$(document).ready(function(){
    const urlParams = new URLSearchParams(window.location.search);
    const productId = urlParams.get('id');
    var headFileLink="http://localhost:8080/files/"
    var rating=''
    $.ajax({
        url: `http://localhost:8080/products/${productId}`,
        method: 'GET'
    }).done(function(msg){
        let html=`
            <div class="btc_shop_single_prod_right_section">
                <div class="btc_shop_sin_pro_icon_wrapper"> `
                    for(let i=1;i<=5;i++){
                        if(i<=msg.data.rating)html+=`<i class="fa fa-star"></i>\n`
                        else html+=`<i class="fa fa-star-o"></i>`
                    }
        html+=      `   <div id="numberOfReview" style = display:inline-block;"></div>
                    <div class="ss_featured_products_box_img_list_cont ss_featured_products_box_img_list_cont_single">
                        <h4>${msg.data.productName}</h4>
                        <p class="shop_pera">${msg.data.categoryName}</p>	<del>$250.00</del>  <ins>$${msg.data.price}</ins>
                        
                    </div>
                </div>
                <div class="btc_shop_prod_quanty_bar">
                    <div class="row">
                        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4 full_width">
                            <div class="cc_ps_quantily_info">
                                <div class="select_number">
                                    <button onclick="changeQty(1); return false;" class="increase"><i class="fa fa-plus"></i>
                                    </button>
                                    <input type="text" name="quantity" value="1" size="2" id="input-quantity" class="form-control" />
                                    <button onclick="changeQty(0); return false;" class="decrease"><i class="fa fa-minus"></i>
                                    </button>
                                </div>
                                <input type="hidden" name="product_id" />
                            </div>
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4 full_width">
                            <div class="cc_ps_color_selectobx">
                                <select>
                                    <option>Size List</option>
                                    <option>32</option>
                                    <option>33</option>
                                    <option>34</option>
                                    <option>35</option>
                                </select><i class="fa fa-caret-down"></i>
                            </div>
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-4 col-xs-4 full_width">
                            <div class="cc_ps_color_selectobx">
                                <select>
                                    <option>Color</option>
                                    <option>Red</option>
                                    <option>Orange</option>
                                    <option>Blue</option>
                                    <option>Green</option>
                                </select><i class="fa fa-caret-down"></i>
                            </div>
                        </div>
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <div class="cc_ps_deliv_main_wrapper">
                                
                                
                                <div class="cc_ps_bottom_cont_heading_wrapper">
                                    <p>Policys :</p>
                                </div>
                                <div class="cc_ps_bottom_cont_list_wrapper">
                                    <ul>
                                        <li>- Security policy (edit with module Customer reassurance)</li>
                                        <li>- Delivery policy (24 Hrs. Delivery policy)</li>
                                        <li>- Return policy (within 15 days return policy)</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>`

        $("#header-right-product").append(html);
        let count=0
        let htmlBigPictureProduct=``;
        let htmlSmallPictureProduct=``;
        for(let i=0;i<msg.data.images.length;i++){
            htmlBigPictureProduct+=`<div class="item" data-hash="#${count}">
                        <img class="small img-responsive" src="${headFileLink}${msg.data.images[i]}" alt="small_img" />
                        </div> \n`;
            htmlSmallPictureProduct+=`<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
								<a class="button secondary url owl_nav" href="#${count++}">
									<img src="${headFileLink}${msg.data.images[i]}" class="img-responsive" alt="nav_img">
								</a>
							</div>`;
        }
        // console.log(htmlBigPictureProduct);
        // console.log(htmlSmallPictureProduct);
        $(`#big-product-picture`).append(htmlBigPictureProduct)    
        $(`#small-product-picture`).append(htmlSmallPictureProduct)  
        
        let description=`<p>${msg.data.description}</p>`
        $(`#description`).append(description)

        rating+=`
            <div class="btc_shop_single_prod_right_section shop_product_single_head shop_product_single_head_respon">
                <h1>product rating</h1>
                <h4>${msg.data.rating} <span>overall rating</span></h4>
            </div>
            <div class="text-accordion shop_pdt_form">
                <div class="progress_section">
                    <div class="progress-wrapper">` 
        $.ajax({
            url: `http://localhost:8080/ratings/products/${productId}`,
            method: 'GET'
        })
        .done(function(msg){
            // console.log(msg)
            let starCount = msg.data.reduce((acc, rating) => {
                let star = rating.star;
                acc[star] = (acc[star] || 0) + 1;
                return acc;
            }, {});
            for (let i = 1; i <= 5; i++) {
                if (!(i in starCount)) {
                    starCount[i] = 0;
                }
            }
            let totalRatings = msg.data.length;
            for (let i=5;i>=1;i--){
                let tmp = totalRatings > 0 ? Math.round((starCount[i] || 0) / totalRatings * 100) : 0;
                rating+=`<div class="progress-item"> <span class="progress-title">${i} star</span>
                <span class="progress-percent pull-right"> ${tmp}%</span>
                <div class="progress">
                    <div class="progress-bar progress-bar-dealy" role="progressbar" aria-valuenow="${tmp}" aria-valuemin="0" aria-valuemax="100"></div>
                </div>\n`
            }
            rating+=`</div>
                </div>
            </div>
            `
            $(`#rating`).append(rating)
            $('.progress_section').on('inview', function(event, visible, visiblePartX, visiblePartY) {
                if (visible) {
                    $.each($('div.progress-bar'), function() {
                        $(this).css('width', $(this).attr('aria-valuenow') + '%');
                    });
                    $(this).off('inview');
                }
            });
            let userRating=``
            msg.data.forEach(element => {
                let star=``
                for(let i=1;i<=5;i++){
                    if(i<=element.star)star+=`<i class="fa fa-star"></i>\n`
                    else star+=`<i class="fa fa-star-o"></i>`
                }
                // console.log(element)
                // console.log(star)
                userRating+=`<div class="sp_comment1_wrapper cmnt_wraper_2">
                    <div class="sp_comment1_img">
                    </div>
                    <div class="sp_comment1_cont">
                        <h3>${element.userFullName}</h3>
                        <p><span>${element.timeCreated} - ${star}</span>
                        </p>
                        <p>${element.desciption}</p>
                    </div>
                </div>`
            });
            $(`#userRating`).append(userRating)
            $(`#numberOfReview`).append("  "+msg.data.length+" review ")
        }) 
    })
    
})