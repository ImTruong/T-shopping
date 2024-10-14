package com.market.tshopping.repository.custom.impl;

import com.market.tshopping.payload.dto.ProductDTO;
import com.market.tshopping.payload.request.ProductSearchingRequest;
import com.market.tshopping.repository.custom.ProductRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.lang.reflect.Field;
import java.util.List;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Page<ProductDTO> searchProductsByCriteria(ProductSearchingRequest productSearchingRequest, Pageable pageable) throws IllegalAccessException {
        String Createquery=createSelectClause()+createJoinClause()+createConditionClause(productSearchingRequest);
        TypedQuery<ProductDTO> query = entityManager.createQuery(Createquery, ProductDTO.class);
        List<ProductDTO> products = query.getResultList();
        int totalProduct=products.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), totalProduct);
        List<ProductDTO> productsInPage = products.subList(start, end);
        return new PageImpl<>(productsInPage, pageable, totalProduct);
    };
    public String createSelectClause(){
        StringBuilder selectClause=new StringBuilder("SELECT p.id" +
                    ",p.productName" +
                    ",p.description" +
                    ",p.brand" +
                    ",p.price" +
                    ",p.stock" +
                    ",c.categoryName" +
                    ",CONCAT(u.firstName,' ',u.lastName) as sellerName" +
                    ",COALESCE(AVG(rp.star), 0) AS rating" +
                    ",COALESCE(MAX(CASE WHEN i.description = 'represent' THEN i.idImage.imageUrl END), 'default_image_url')" +
                    " FROM product p");
        return selectClause.toString();
    }
    public String createJoinClause(){
        StringBuilder joinClause=new StringBuilder(" LEFT JOIN p.ratingProducts rp"
                + " LEFT JOIN p.images i"
                + " LEFT JOIN p.user u"
                + " LEFT JOIN p.category c");
        return joinClause.toString();
    }
    public String createConditionClause(ProductSearchingRequest productSearchingRequest) throws IllegalAccessException{
        StringBuilder whereClause=new StringBuilder(" WHERE 1=1 ");
//        whereClause.append(" AND i.description = 'represent' ");
        StringBuilder havingClause=new StringBuilder(" HAVING 1=1 ");
        Field[] fields = productSearchingRequest.getClass().getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            Object value=field.get(productSearchingRequest);
            if(value!=null){
                String fieldName=field.getName();
                if (value instanceof List) {
                    List<?> list = (List<?>) value;
                    if (!list.isEmpty()) {

                        whereClause.append(" AND ( ");
                        for(int e=0;e<list.size();e++){
                            if(e>0) whereClause.append(" OR ");
                            whereClause.append(" p." + fieldName);
                            if(fieldName.equals("category"))whereClause.append(".id");
                            if(list.get(e) instanceof String)whereClause.append(" = "+ "'"+list.get(e)+"'"+" ");
                            else whereClause.append(" = "+list.get(e)+" ");
                        }
                        whereClause.append(" ) ");
                    }
                }
                else{
                    if(fieldName.equals("productName")){
                        if(!value.equals("")){
                            whereClause.append(" AND ");
                            if(fieldName.equals("productName"))whereClause.append(" p." + fieldName + " like '%" + value +"%' ");
                            else whereClause.append(" p." + fieldName + " = " + value +" ");
                        };
                    }
                    else if(fieldName.equals("minPrice") || fieldName.equals("maxPrice")){
                        whereClause.append(" AND p.price ");
                        if(fieldName.equals("minPrice"))whereClause.append(" >= " + value);
                        else whereClause.append(" <= " + value);
                    }
                    else if(fieldName.equals("star")){
                        havingClause.append(" AND ");
                        havingClause.append(" COALESCE(AVG(rp.star), 0) >= "+value+" ");
                    }
                }
            }
        }
        if(productSearchingRequest.getStar()==null){
            havingClause.append(" AND ");
            havingClause.append("COALESCE(AVG(rp.star), 0) >= "+"0"+" ");
        }
        StringBuilder result =new StringBuilder();
        result.append(whereClause+" GROUP BY p.id "+havingClause);
        return result.toString();
    }
}
