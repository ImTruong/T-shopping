$(document).ready(function () {
	$.ajax({
			method: "GET",
			url: "http://localhost:8080/categories",
		})
		.done(function (msg) {
			if (msg.success) {
				var html = "";
				msg.data.forEach(element => {
					html = html + `
                    <li class="yamm-tfw menu-item menu-item-has-children animate-dropdown dropdown"> <a
									title="Cameras, Audio &amp; Video" data-hover="dropdown"
									href="product-category.html" data-toggle="dropdown" class="dropdown-toggle"
									aria-haspopup="true" category-id="${element.id}"><i class="fa fa-female" aria-hidden="true"></i>${element.categoryName}</a>
							</li>`
				});
				html = html + `<li class="yamm-tfw menu-item menu-item-has-children animate-dropdown dropdown"> <a
				title="Car, Motorbike &amp; Industrial" data-hover="dropdown"
				href="product-category.html" data-toggle="dropdown" class="dropdown-toggle"
				aria-haspopup="true"><i class="fa fa-plus" aria-hidden="true"></i> More Category</a>
		</li>`
				$(`#head-category`).append(html);
			} else {
				alert("Fail to load categories");
			}
		})

})