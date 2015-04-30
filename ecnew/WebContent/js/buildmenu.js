/**
 * 
 */

$(document).ready(

		function() {

			// utility method to make AJAX GET requests.
			makeGETCall = function(url, handler, selector) {
				// submit using AJAX.
				$.ajax({
					type : "GET",
					url : url,
					success : function(data) {
						console.log(JSON.stringify(data));
						handler(data, selector);
					},
					dataType : "json",
					contentType : "application/json",
				});
			};

			menuHandler = function(data, selector) {
				var message = data.statusMsg;
				var responseArray = data.results;
				var tableStr = "<ul>";
				if (message === 'SUCCESS') {
					$.each(responseArray, function(index, value) {
						tableStr = tableStr + "<li><a href='" + value.href
								+ "'/>" + value.displayName + "</a></li>";

					});

					tableStr = tableStr + "</ul>";
					console.log(tableStr);
					$("#nav_wrapper").html(tableStr);
				} else {
					console.log("error happened");

				}
			};

			buildMenu = function() {
				makeGETCall(ctx+"/service/permissions", menuHandler, "#nav_wrapper");
			};

			buildMenu();

		});