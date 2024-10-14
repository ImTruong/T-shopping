$(document).ready(function () {
    var headFileLink="http://localhost:8080/files/"
    var numberOfBar = 1
    callSearch();
    function sideFilterHtml(filterName, data, property) {
        let html =
            `<div class="link cc_product_heading">` + filterName + `<i class="fa fa-chevron-down"></i>
            </div>
                <ul class="submenu">
                    <li>
                        <div class="content">
                            <div class="box">`;
        if (data.every(item => typeof item === 'string')) {
            data.forEach(element => {
                html = html +
                    `<p class="cc_pc_color${element.id}">
                    <input type="checkbox" id="c${numberOfBar}${property}" name="${filterName}" value="${element}">
                            <label for="c${numberOfBar}${property}">${element}</label>
                    </p>`
                property++;
            });
        } else {
            data.forEach(element => {
                html = html +
                    `<p class="cc_pc_color${element.id}">
                    <input type="checkbox" id="c${numberOfBar}${element.id}" name="${filterName}" value="${element.id}">
                            <label for="c${numberOfBar}${element.id}">${element[property]}</label>
                    </p>`
            });
        }

        html = html + `                         </div>
                                            </div>
                                        </li>
                                    </ul>`
        numberOfBar++
        return html;
    }
    function callSearch(){
        let selectedCategories = [];
        let selectedBrand = [];
        let price = $('input[name="PRICE"]').val().replace(/\$/g, '').split('-');
        let minPrice = parseInt(price[0].trim());
        let maxPrice = parseInt(price[1].trim());
        let rating = $('input[name="RATING"]:checked').val()
        let productName = $('input[name="PRODUCTNAME"]').val()
        $('input[name="CATEGORY"]:checked').each(function () {
            selectedCategories.push($(this).val());
        });
        $('input[name="BRAND"]:checked').each(function () {
            selectedBrand.push($(this).val());
        });
        $(`#product`).empty();
        $.ajax({
            url: 'http://localhost:8080/products/search',
            method: 'GET',
            data: {
                "productName": productName,
                "category": selectedCategories,
                "brand": selectedBrand,
                "minPrice": minPrice,
                "maxPrice": maxPrice,
                "star": rating
            }
        })
        .done(function (msg) {
            let html = ""
            msg.data.content.forEach(element => {
                let localHref=window.location.href.substring(0, window.location.href.lastIndexOf('/') + 1);
                html = html +
                `
                <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12 prs_upcom_slide_first" bis_skin_checked="1">
                        <div class="ss_featured_products_box" bis_skin_checked="1">
                            <a href="${localHref}product.html?id=${element.id}" class="product-link">
                                <input type="hidden" name="productId" value="${element.id}">

                                <div class="ss_featured_products_box_img" bis_skin_checked="1">	<span class="ss_tag">new</span>
                                    <span class="ss_offer">20% off</span>
                                    <img src="${headFileLink}${element.images[0]}" alt="Product" class="img-responsive product-image">
                                    
                                </div>
                                <div class="ss_feat_prod_cont_heading_wrapper" bis_skin_checked="1">
                                    <h4><a href="#">${element.productName}</a></h4>
                                    <p>${element.categoryName}</p>	<del>$250.00</del>  <ins>$${element.price}</ins>
                                </div>
                            </a>
                            <div class="ss_featured_products_box_footer" bis_skin_checked="1">
                                
                                <ul>
                                    
                                    <li>
                                        <button class="ss_btn">Add To Bag</button>
                                    </li>
                                    
                                </ul>
                            </div>
                        </div>
                    </div>
                `
            });
            $(`#product`).append(html)
            
        })
    }
    $.ajax({
            method: "GET",
            url: "http://localhost:8080/categories"
        })
        .done(function (msg) {
            if (msg.success) {
                let html = sideFilterHtml("CATEGORY", msg.data, "categoryName");
                $(`#category`).append(html);
            } else {
                alert("Fail to load categories");
            }
        })
    $.ajax({
            method: "GET",
            url: "http://localhost:8080/products/brand"
        })
        .done(function (msg) {
            if (msg.success) {
                let html = sideFilterHtml("BRAND", msg.data, 1);
                $(`#brand`).append(html);
            } else {
                alert("Fail to load Product-brand");
            }
        })
    $('#searchButton').click(function () {
        callSearch();
    });
})