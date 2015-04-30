$(document)
		.ready(

				function() {

					fetchPending = function() {
						var url = ctx + "/service/order/list/pending";
						makeGETCall(url, handlePendingFetch);
					};

					fetchNotactioned = function() {
						var url = ctx + "/service/order/list/notaction";
						makeGETCall(url, handleNotactionedFetch);
					};

					handleListingDashboard = function() {
						fetchPending();
						fetchNotactioned();
					};

					handlePendingFetch = function(data) {
						var message = data.statusMsg;
						var responseArray = data.results;
						var htmlStr = "";
						if (message === 'SUCCESS' && responseArray != null
								&& responseArray.length > 0) {
							$
									.each(
											responseArray,
											function(index, value) {
												htmlStr = htmlStr
														.concat('<p><a href="#" onclick="handleOrderIDClick('
																+ value.orderID
																+ ')">'
																+ value.orderDisplay
																+ '</a></p>');
											});
						} else {
							htmlStr = "<p> No pending orders</p>";
						}

						$("#pending").html(htmlStr);
					};

					handleNotactionedFetch = function(data) {
						var message = data.statusMsg;
						var responseArray = data.results;
						var htmlStr = "";
						if (message === 'SUCCESS' && responseArray != null
								&& responseArray.length > 0) {
							$
									.each(
											responseArray,
											function(index, value) {
												htmlStr = htmlStr
														.concat('<p><a href="#" onclick="handleOrderIDClick('
																+ value.orderID
																+ ')">'
																+ value.orderDisplay
																+ '</a></p>');
											});
						} else {
							htmlStr = "<p> No orders to action</p>";
						}

						$("#notactioned").html(htmlStr);
					};

					$("#ops-fragment-link").accordion({
						heightStyle : 'panel',
						collapsible : 'true',
						active : 'true',
						active : 0
					});

					handleOrdersCallback = function(data, selector) {
						var message = data.statusMsg;
						var sltr = selector.substr(1);
						var responseArray = data.results;
						var tableStr = "";
						if (message === 'SUCCESS') {
							tableStr = tableStr.concat('<table id="' + sltr
									+ 'ordersTable" class="tablesorter">');
							tableStr = tableStr.concat('<thead>');
							tableStr = tableStr.concat('<th>ORDERID</th>');
							tableStr = tableStr.concat('<th>PHONE</th>');
							tableStr = tableStr
									.concat('<th>CUSTOMER NAME</th>');
							tableStr = tableStr.concat('<th>PICK DATE</th>');
							tableStr = tableStr.concat('<th>PICK TIME</th>');
							tableStr = tableStr.concat('<th>ORDER STATUS</th>');
							tableStr = tableStr.concat('<th>UPDATED BY</th>');
							console.log(selector+" " +sltr);
							if (sltr === 'openOrdersDiv') {
								tableStr = tableStr
										.concat('<th>BOOKING TIME</th>');
							} else if (sltr === 'forwardedOrdersDiv') {
								tableStr = tableStr
										.concat('<th>FORWARDED TIME</th>');
							}

							tableStr = tableStr.concat('</thead>');
							$
									.each(
											responseArray,
											function(index, value) {
												tableStr = tableStr
														.concat('<tr>');
												tableStr = tableStr
														.concat('<td align="center" width="8%"><a href="#" onclick="handleOrderIDClick('
																+ value.orderID
																+ ')">'
																+ value.orderDisplay
																+ '</a></td>');
												tableStr = tableStr
														.concat('<td align="center" width="13%">'
																+ value.phoneNo
																+ '</td>');
												tableStr = tableStr
														.concat('<td align="center" width="24%">'
																+ value.firstName
																+ ' '
																+ value.lastName
																+ '</td>');
												tableStr = tableStr
														.concat('<td align="center" width="10%">'
																+ value.pickUpDate
																+ '</td>');
												tableStr = tableStr
														.concat('<td align="center" width="10%">'
																+ value.pickUpTime
																+ '</td>');
												tableStr = tableStr
														.concat('<td align="center" width="10%">'
																+ value.status
																+ '</td>');
												tableStr = tableStr
														.concat('<td align="center" width="10%">'
																+ value.updatedBy
																+ '</td>');
												if (sltr === 'openOrdersDiv' || sltr === 'forwardedOrdersDiv') {
												tableStr = tableStr
														.concat('<td align="center" width="15%">'
																+ value.updatedDateStr
																+ '</td>');
												}

												tableStr = tableStr
														.concat('</tr>');
											});

							tableStr = tableStr.concat('</table>');
							$(selector).html(tableStr);
							$("#" + sltr + "ordersTable").tablesorter();
							return tableStr;
						}

					};

					// when the Open orders accordion is activated
					fetchOpenOrders = function() {
						// submit using AJAX.
						var url = ctx + "/service/order/list/OPEN";
						makeGETCall(url, handleOrdersCallback, "#openOrdersDiv");
					};

					fetchOpenOrders();

					// handler for accordion header clicks. To get forwarded
					// orders.
					$("#forwardedOrders").click(function() {
						fetchForwardedOrders();
					});

					// when the Open orders accordion is activated
					fetchForwardedOrders = function() {
						// submit using AJAX.
						var url = ctx + "/service/order/list/FORWARDED";
						makeGETCall(url, handleOrdersCallback,
								"#forwardedOrdersDiv");
					};

					// handler for accordion header clicks. To get picked
					// orders.
					$("#pickedOrders").click(function() {
						fetchPickedOrders();
					});

					// when the Open orders accordion is activated
					fetchPickedOrders = function() {
						// submit using AJAX.
						var url = ctx + "/service/order/list/PICKED";
						makeGETCall(url, handleOrdersCallback,
								"#pickedOrdersDiv");
					};

					handleListingDashboard();

				});
