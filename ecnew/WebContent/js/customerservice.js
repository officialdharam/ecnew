$(document)
		.ready(

				function() {

					$("#cs-fragment-link").accordion({
						heightStyle : 'panel',
						collapsible : 'true',
						active : 'true',
						active : 0
					});

					$("#phoneNumber").focusout(
							function() {
								var phone = $("#phoneNumber").val();

								if (phone != null && phone != undefined
										&& phone.length === 10) {
									var url = ctx + "/service/customer?phone=" + phone;
									makeGETCall(url, handleFetchCustomer);
								}
							});

					handleFetchCustomer = function(data) {
						var message = data.statusMsg;
						var customer = data.results[0];
						if (message === 'SUCCESS') {
							$("#firstName").val(customer.firstName);
							$("#lastName").val(customer.lastName);
							$("#emailId").val(customer.email);
							$("#pincode").val(customer.pincode);
							$("#address").val(customer.address);
						} 
					};

					$('#pickupTime').ptTimeSelect();

					// add the date picker to the date div.
					$("#date").datepicker(
							{
								minDate : new Date(),
								maxDate : new Date(new Date().getTime() + 24
										* 6 * 60 * 60 * 1000)
							});
					// on the click of the Place Order button make a POST
					// request.
					$("#placeOrderBtn").click(
							function() {
								if (validPlaceOrderForm()) {
									// verify the form first
									var formData = JSON.stringify(jQuery(
											'#orderForm').serializeArray());

									// submit using AJAX.
									var url = ctx + "/service/order";
									makePOSTCall(url, formData,
											handlePlaceOrderCallback);
								}
								return;
							});

					// handler for place order call back
					handlePlaceOrderCallback = function(data) {
						var message = data.statusMsg;
						var orderID = data.results[0];
						var msg = null;
						if (message === 'SUCCESS') {
							msg = "Order with ORDER ID " + orderID
									+ " created successfully.";
							$('#orderForm').trigger("reset");
						} else if (message === 'PINCODE_NOT_SERVICEABLE_MESSAGE') {
							msg = "Pincode is not serviceable";
						}
						$("#successLabel").text(msg);
					};

					// on click of search order buttons for a customer phone
					// number.
					$("#searchOrdersForCustomer")
							.click(
									function() {
										var phoneNo = $("#phoneNumberSearch").val();
										if (phoneNo != undefined && phoneNo != null && phoneNo.length === 10) {
											var url = ctx
													+ "/service/order?phoneNumber="
													+ phoneNo;
											makeGETCall(url,
													handleSearchOrdersForPhoneCallback);
										}else{
											document.getElementById("errorNumberSearch").style.display ="inline";
										}
									});

					// handler for search order button.
					handleSearchOrdersForPhoneCallback = function(data) {
						var message = data.statusMsg;
						var responseArray = data.results;
						var tableStr = "";
						if (message === 'SUCCESS' && responseArray != null
								&& responseArray.length > 0) {

							$("#failureLabel").text("");

							var customer = responseArray[0];
							tableStr = tableStr.concat('<h5>Phone : '
									+ customer.phoneNo + '</h5>');
							tableStr = tableStr.concat('<h5>Customer Name : '
									+ customer.firstName + " "
									+ customer.lastName + '</h5>');
							tableStr = tableStr
									.concat('<table id="orderTablePhoneSearch" class="tablesorter">');
							tableStr = tableStr.concat('<thead>');
							tableStr = tableStr.concat('<th>ORDERID</th>');
							tableStr = tableStr.concat('<th>BILL AMOUNT</th>');
							tableStr = tableStr.concat('<th>PICK UP DATE</th>');
							tableStr = tableStr.concat('<th>PICK UP TIME</th>');
							tableStr = tableStr.concat('<th>ORDER STATUS</th>');
							tableStr = tableStr.concat('</thead><tbody>');
							$
									.each(
											responseArray,
											function(index, value) {
												tableStr = tableStr
														.concat('<tr>');
												tableStr = tableStr
														.concat('<td align="center" width="20%"><a href="#" onclick="handleOrderIDClick('
																+ value.orderID
																+ ')">'
																+ value.orderDisplay
																+ '<a/></td>');
												tableStr = tableStr
														.concat('<td align="center" width="20%">'
																+ value.amount
																+ '</td>');
												tableStr = tableStr
														.concat('<td align="center" width="20%">'
																+ value.pickUpDate
																+ '</td>');
												tableStr = tableStr
														.concat('<td align="center" width="20%">'
																+ value.pickUpTime
																+ '</td>');
												tableStr = tableStr
														.concat('<td align="center" width="20%">'
																+ value.status
																+ '</td>');
												tableStr = tableStr
														.concat('</tr>');
											});

							tableStr = tableStr.concat('</tbody></table>');
							$("#customerOrdersDiv").html(tableStr);
							$("#orderTablePhoneSearch").tablesorter();

						} else {
							$("#failureLabel")
									.text(
											"The phone number doesn't exist in the system.");
						}
					};

					validPlaceOrderForm = function() {
						var phone = $("#phoneNumber").val();
						var pincode = $("#pincode").val();
						var address = $("#address").val();
						var pickuptime = $("#pickupTime").val();
						var date = $("#date").val();
						var status = true;
						if (phone === undefined || phone === null
								|| phone.length < 10) {
							$("#errorNumber").text("10 digits required.");
							$("#errorNumber").css("display", "inline");
							status = false;
						} else {
							$("#errorNumber").css("display", "none");
						}

						if (pincode === undefined || pincode === null
								|| pincode.length < 6) {
							$("#errorNumberPin").text("6 digits required.");
							$("#errorNumberPin").css("display", "inline");
							status = false;
						} else {
							$("#errorNumberPin").css("display", "none");
						}

						if (address === undefined || address === null
								|| address.length === 0) {
							$("#addressError").text("address is required.");
							$("#addressError").css("display", "inline");
							status = false;
						} else {
							$("#addressError").css("display", "none");
						}

						if (pickuptime === undefined || pickuptime === null
								|| pickuptime.length === 0) {
							$("#pickTimeError")
									.text("pickup time is required.");
							$("#pickTimeError").css("display", "inline");
							status = false;
						} else {
							$("#pickTimeError").css("display", "none");
						}

						if (date === undefined || date === null
								|| date.length === 0) {
							$("#pickDateError").text("date is required.");
							$("#pickDateError").css("display", "inline");
							status = false;
						} else {
							$("#pickDateError").css("display", "none");
						}

						return status;
					};

				});
