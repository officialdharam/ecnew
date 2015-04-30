/**
 * 
 */
$(document)
		.ready(

				function() {

					var orderID = 0;

					// utility method to check if the string is null or empty
					isEmpty = function(str) {
						if (str == undefined || str.length == 0) {
							return true;
						}

						return false;
					};

					prepareOrderContentTable = function(value) {
						var tableStr = "";
						tableStr = tableStr.concat('<table>');
						tableStr = tableStr.concat('<thead>');
						tableStr = tableStr.concat('<th>CATEGORY</th>');
						tableStr = tableStr.concat('<th>QUANTITY</th>');
						tableStr = tableStr.concat('<th>DESCRIPTION</th>');
						tableStr = tableStr.concat('</thead>');
						var categories = value.items;
						$.each(categories, function(index, catValue) {
							tableStr = tableStr.concat('<tr>');
							tableStr = tableStr
									.concat('<td align="center" width="25%">'
											+ catValue.name + '</td>');
							tableStr = tableStr
									.concat('<td align="center" width="25%">'
											+ catValue.quantity + '</td>');
							tableStr = tableStr
									.concat('<td align="center" width="50%">'
											+ catValue.description + '</td>');
							tableStr = tableStr.concat('</tr>');
						});

						tableStr = tableStr.concat('</table>');
						return tableStr;
					};

					statusList = function(selected) {
						var valueStr = '<select name="status" id="statusOrder" onChange="changeOrder()">';

						if (group === "WAREHOUSE" || group === "ADMIN"
								|| group === "MANAGER") {
							valueStr = valueStr
									.concat('<option value="CLOSED"');
							valueStr = valueStr.concat('>CLOSED</option>');
						}

						valueStr = valueStr.concat('<option value="OPEN"');
						if (selected === "OPEN") {
							valueStr = valueStr.concat(' selected="OPEN"');
						}
						valueStr = valueStr.concat('>OPEN</option>');

						valueStr = valueStr.concat('<option value="FORWARDED"');
						if (selected === "FORWARDED") {
							valueStr = valueStr.concat(' selected="FORWARDED"');
						}
						valueStr = valueStr.concat('>FORWARDED</option>');

						valueStr = valueStr.concat('<option value="PICKED"');
						if (selected === "PICKED") {
							valueStr = valueStr.concat(' selected="PICKED"');
						}
						valueStr = valueStr.concat('>PICKED</option>');

						return valueStr;

					};

					fetchDeviceForFE = function() {
						var fe = $("#fes option:selected").val();
						var warehouseName = $("#currentCenterName").text();
						var url = ctx + "/service/warehouse/device?warehouseName="
								+ warehouseName + "&feName=" + fe;
						makeGETCall(url, fetchFEDevicehandler, null);
					};

					fetchFEDevicehandler = function(data) {
						var message = data.statusMsg;
						var deviceID = '';
						if (message === 'SUCCESS') {
							if (data.results.length === 1) {
								deviceID = '<input type="text" name="deviceID" id="deviceIDForFE" value="'+data.results[0]+'" readonly/>';
							} else {
								deviceID = prepareDevice(data.results);
							}

						} else {
							console.log('Failure in fetching devices.');
							deviceID = '<input type="text" name="deviceID" id="deviceIDForFE"/>';
						}
						
						$("#devicesID").html(deviceID);
					};

					prepareDevice = function(devices) {
						var valueStr = "";
						valueStr = '<select name="deviceID" id="devices">';
						for (var i = 0; i < devices.length; i++) {
							valueStr = valueStr.concat('<option value="');
							valueStr = valueStr.concat(devices[i]);
							valueStr = valueStr.concat('">');
							valueStr = valueStr.concat(devices[i]);
							valueStr = valueStr.concat('</option>');

						}
						valueStr = valueStr.concat('</select>');
						return valueStr;
					};

					feList = function(data) {

						var message = data.statusMsg;
						var valueStr = "";
						if (message === 'SUCCESS') {
							valueStr = '<select name="fe" id="fes" onchange="fetchDeviceForFE()">';
							for (var i = 0; i < data.results.length; i++) {
								valueStr = valueStr.concat('<option value="');
								valueStr = valueStr.concat(data.results[i]);
								valueStr = valueStr.concat('">');
								valueStr = valueStr.concat(data.results[i]);
								valueStr = valueStr.concat('</option>');

							}
							valueStr = valueStr.concat('</select>');
						}
						return valueStr;

					};

					sumbitPickup = function() {
						$("#addPickForm").submit();

					};

					pickOrderStatus = function(data) {
						var fedata = feList(data);
						var warehouseName = $("#currentCenterName").text();
						var summary = '<form id="addPickForm" method="post" action="'
								+ ctx
								+ '/service/warehouse/pickOrder"><div class="inputarea" ><label  class="mandatorylabel">Field Executive<span>*</span></label>'
								+ fedata
								+ '</div><input type="hidden" name="warehouse" value="'
								+ warehouseName
								+ '"/><input type="hidden" name="orderId" value="'
								+ orderID
								+ '"></div><div class="inputarea" ><label  class="mandatorylabel">Bill Amount <span>*</span></label><input type="text" name="billAmount" id="billAmount" /></div><div class="inputarea" ><label  class="mandatorylabel">Device ID </label><div id="devicesID" style="display:inline;margin-left:0px;"></div></div><div class="inputarea" ><label  class="mandatorylabel">Order Contents </label></div>'
								+ '<table><tr><td> NEWS PAPER (Kgs)</td><td><input type="text" name="newsPaper"/></td><td>BOOKS n COPIES (Kgs)</td><td><input type="text" name="books"/></td></tr><tr><td>IRON (Kgs)</td><td> <input type="text" name="iron"/></td><td>PLASTIC (Kgs)</td><td><input type="text" name="plastic"/></td></tr><tr><td>GLASS BOTTLE (Pcs)</td><td><input type="text" name="gBottle"/></td><td>BEER BOTTLE (Pcs)</td><td><input type="text" name="bBottle"/></td></tr><tr><td>CARTON (Kgs)</td><td><input type="text" name="carton"/></td><td>ALUMINIUM (Kgs)</td><td><input type="text" name="aluminium"/></td></tr><tr><td>STEEL (Kgs)</td><td><input type="text" name="steel"/></td><td>COPPER (Kgs)</td> <td><input type="text" name="copper"/></td></tr><tr><td>BRASS (Kgs)</td><td><input type="text" name="brass"/></td><td colspan="2"><input type="button" id="pickBtn" value="Save" onClick="sumbitPickup()"></td></tr></table></form>';

						$("#orderSummary").html(summary);
						$("#orderSummary").dialog({
							height : 'auto',
							width : 800,
							modal : true,
							draggable : false,
							resizable : false,
							autoOpen : false,
							show : {
								effect : "fade",
								duration : 500
							},
							hide : {
								effect : "fade",
								duration : 500
							},
						});
						$("#orderSummary").dialog('open');
						$("#fes").trigger('onchange');

					};

					changeOrder = function() {
						var str = $("#statusOrder option:selected").val();
						if (str.trim() === "PICKED") {
							var warehouseName = $("#currentCenterName").text();
							var url = ctx
									+ "/service/warehouse/fetchExecutives?warehouseName="
									+ warehouseName;
							var form = jQuery('#updateOrder').serializeArray();
							orderID = form[0].value;
							makeGETCall(url, pickOrderStatus, null);

						}
						;
					};

					makePOSTCall = function(url, data, handler) {
						console.log(data);

						// submit using AJAX.
						$.ajax({
							type : "POST",
							url : url,
							data : data,
							success : handler,
							dataType : "json",
							contentType : "application/json"
						});
					};

					// utility method to make AJAZ POST requests.
					makePUTCall = function(url, data, handler) {
						// submit using AJAX.
						$.ajax({
							type : "PUT",
							url : url,
							data : data,
							success : handler,
							dataType : "json",
							contentType : "application/json"
						});
					};

					// utility method to make AJAX GET requests.
					makeGETCall = function(url, handler, selector) {
						// submit using AJAX.
						$.ajax({
							type : "GET",
							url : url,
							success : function(data) {
								if (handler === null || handler === undefined)
									return data;
								else
									handler(data, selector);
							},
							error : function() {
								console.log("failed");
							},
							dataType : "json",
							contentType : "application/json",
						});
					};

					// handler for place order call back
					updateOrderCallback = function(data) {
						var message = data.statusMsg;
						if (message === 'SUCCESS') {
							$("#updateSuccessDiv").removeClass(
									"updateInVisible");
							setTimeout(function() {
								$("#orderSummary").dialog('close');
								location.reload(true);
							}, 2000);
						} else {
							$("#updateSuccessDiv").removeClass(
									"updateInVisible");
							$("#updateSuccessDiv").addClass("updateInVisible");

						}
						/*
						 * $('#orderForm').trigger("reset");
						 * $("#successLabel").text(msg);
						 */
					};

					handleUpdateOrderBtnClick = function() {

						// verify the form first
						var formData = JSON.stringify(jQuery('#updateOrder')
								.serializeArray());

						// submit using AJAX.
						var url = ctx + "/service/order";
						makePUTCall(url, formData, updateOrderCallback);
					};

					prepareOrderSummary = function(order) {
						var summaryStr = '<form id="updateOrder"><div id="orderSummaryFormDiv"><label>Order Number </label><label>'
								+ order.orderDisplay + '</label><br/>';
						summaryStr = summaryStr
								.concat('<input type="hidden" name="orderID" value="'
										+ order.orderID + '"/>');
						if (!isEmpty(order.firstName))
							summaryStr = summaryStr
									.concat('<label>Customer Name </label><label>'
											+ order.firstName
											+ ' '
											+ order.lastName + '</label><br/>');
						if (!isEmpty(order.address))
							summaryStr = summaryStr
									.concat('<label>Address </label><label>'
											+ order.address + '</label><br/>');

						if (!isEmpty(order.phoneNo))
							summaryStr = summaryStr
									.concat('<label>Phone Number </label><label>'
											+ order.phoneNo + '</label><br/>');

						summaryStr = summaryStr
								.concat('<label>Order Status </label>'
										+ statusList(order.status));

						if (!isEmpty(order.orderDate))
							summaryStr = summaryStr
									.concat('<label>Order Date </label><label>'
											+ order.orderDate + '</label><br/>');

						if (!isEmpty(order.pickUpDate))
							summaryStr = summaryStr
									.concat('<label>Picked Up At </label><label>'
											+ order.pickUpDate
											+ ' '
											+ order.pickUpTime
											+ '</label><br/>');

						if (order.deviceID != undefined
								&& order.deviceID != '0')
							summaryStr = summaryStr
									.concat('<label>Device ID </label><label>'
											+ order.deviceID + '</label><br/>');

						if (order.items != undefined && order.items.length > 0) {
							summaryStr = summaryStr
									.concat('<label><b>Order Contents</b>'
											+ '</label><br/>');
							var contentStr = prepareOrderContentTable(order);
							summaryStr = summaryStr.concat(contentStr);
						}

						summaryStr = summaryStr
								.concat('<input type="button" onClick="handleUpdateOrderBtnClick()" value="Update"/><div id="updateSuccessDiv" class="updateSuccess updateInVisible">Order Updated successfully</div>'
										+ '</div></form>');
						return summaryStr;
					};

					showSummary = function(data) {
						var message = data.statusMsg;
						var order = data.results[0];
						if (message === 'SUCCESS') {
							var summary = prepareOrderSummary(order);
							$("#orderSummary").html(summary);
							$("#orderSummary").dialog({
								height : 'auto',
								width : 600,
								modal : true,
								draggable : false,
								resizable : false,
								autoOpen : false,
								show : {
									effect : "fade",
									duration : 500
								},
								hide : {
									effect : "fade",
									duration : 500
								},
							});
							$("#orderSummary").dialog('open');
						}
					};

					handleOrderIDClick = function(orderID) {
						var url = ctx + "/service/order/" + orderID;
						makeGETCall(url, showSummary);

					};

					$("#signout").click(function() {
						var url = ctx + "/service/signout/";
						location.href = url;
					});

					$("#changePasswordLink").click(function() {
						changePassword();

					});

					changePassword = function() {
						var url = ctx + "/service/changePassword";
						$("#changePassword")
								.html(
										'<form method="post" action="'
												+ url
												+ '"><div id="changepasswordForm"  class="customerFormDetails "><div class="inputarea mandatorylabel"><label>Current Password</label><span>*</span><input type="password" name="oldpassword" id="oldpassword" size="20" maxlength="10" /></div><div class="inputarea mandatorylabel"> <label>New Password </label><span>*</span><input type="password" name="newpassword" size="20" /></div><div class="inputarea mandatorylabel"> <label>Confirm Password </label><span>*</span><input type="password" name="confirmPassword" size="20" /></div> <input	type="submit" value="Change Password" /></div></form>');
						$("#changePassword").dialog({
							height : 'auto',
							width : 600,
							modal : true,
							draggable : false,
							resizable : false,
							autoOpen : false,
							show : {
								effect : "fade",
								duration : 500,
							},
							hide : {
								effect : "fade",
								duration : 500,
							}
						});

						$("#changePassword").dialog('open');

					};

					$("#searchBulkOrders").click(function() {
						var orderids = $("#orderids").val();
						orderids = orderids.trim();
						var indexOfComma = orderids.indexOf(',');
						var indexOfSpace = orderids.indexOf(' ');
						var res1;
						if (indexOfComma > -1) {
							res1 = orderids.split(",");
						} else if (indexOfSpace > -1) {
							res1 = orderids.split(" ");
						} else {
							var array = new Array();
							array.push(orderids);
							res1 = array;
						}

						var myJsonString = JSON.stringify(res1);
						var url = ctx + "/service/searchOrders";
						makePOSTCall(url, myJsonString, searchOrderHandlers);
					});

					searchOrderHandlers = function(data) {
						var orders = handleSearchOrdersCallback(data);
						$("#bulkOrders").html(orders);
						$("#bulkOrders").dialog({
							height : 'auto',
							width : 850,
							modal : true,
							draggable : false,
							resizable : false,
							autoOpen : false,
							show : {
								effect : "fade",
								duration : 500
							},
							hide : {
								effect : "fade",
								duration : 500
							},
						});
						$("#bulkOrders").dialog('open');
						$("#bulkOrders a").css('cursor', 'default');
						$("#bulkOrders a").css('text-decoration', 'none');
						$("#searchOrdersTable").tablesorter();
					};

					handleSearchOrdersCallback = function(data) {
						var message = data.statusMsg;
						/* var sltr = selector.substr(1); */
						var responseArray = data.results;
						var tableStr = "";
						if (message === 'SUCCESS' && responseArray != null
								&& responseArray.length > 0) {
							tableStr = tableStr
									.concat('<table id="searchOrdersTable" class="tablesorter">');
							tableStr = tableStr.concat('<thead>');
							tableStr = tableStr.concat('<th>ORDERID</th>');
							tableStr = tableStr.concat('<th>PHONE</th>');
							tableStr = tableStr
									.concat('<th>CUSTOMER NAME</th>');
							tableStr = tableStr.concat('<th>PICK UP DATE</th>');
							tableStr = tableStr.concat('<th>PICK UP TIME</th>');
							tableStr = tableStr.concat('<th>ORDER STATUS</th>');
							/* tableStr = tableStr.concat('<th>UPDATED BY</th>'); */
							tableStr = tableStr.concat('</thead>');
							$
									.each(
											responseArray,
											function(index, value) {
												tableStr = tableStr
														.concat('<tr>');
												tableStr = tableStr
														.concat('<td align="center" width="10%"><a href="#" onclick="handleOrderIDClick('
																+ value.orderID
																+ ')">'
																+ value.orderDisplay
																+ '</a></td>');
												tableStr = tableStr
														.concat('<td align="center" width="10%">'
																+ value.phoneNo
																+ '</td>');
												tableStr = tableStr
														.concat('<td align="center" width="30%">'
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
														.concat('<td align="center" width="15%">'
																+ value.status
																+ '</td>');
												/*
												 * tableStr = tableStr .concat('<td align="center" width="15%">' +
												 * value.updatedBy + '</td>');
												 */
												tableStr = tableStr
														.concat('</tr>');
											});

							tableStr = tableStr.concat('</table>');
							return tableStr;
						} else {
							return '<div class="noDataPopup"> No orders found for the given order IDs </div>';
						}

					};

					validFirstName = function(e) {
						var specialKeys = new Array();
						specialKeys.push(8); // Backspace
						var keyCode = e.which ? e.which : e.keyCode;
						var ret = (((keyCode >= 65 && keyCode <= 90) || (keyCode >= 97 && keyCode <= 122)) || specialKeys
								.indexOf(keyCode) != -1);
						document.getElementById("errorFirstName").style.display = ret ? "none"
								: "inline";
						return ret;
					};

					validLastName = function(e) {
						var specialKeys = new Array();
						specialKeys.push(8); // Backspace
						var keyCode = e.which ? e.which : e.keyCode;
						var ret = (((keyCode >= 65 && keyCode <= 90) || (keyCode >= 97 && keyCode <= 122)) || specialKeys
								.indexOf(keyCode) != -1);
						document.getElementById("errorLastName").style.display = ret ? "none"
								: "inline";
						return ret;
					};

					IsNumeric = function(e) {
						var specialKeys = new Array();
						specialKeys.push(8); // Backspace
						var keyCode = e.which ? e.which : e.keyCode;
						var ret = ((keyCode >= 48 && keyCode <= 57) || specialKeys
								.indexOf(keyCode) != -1);
						document.getElementById("errorNumber").style.display = ret ? "none"
								: "inline";
						return ret;
					};

					IsNumericSearch = function(e) {
						var specialKeys = new Array();
						specialKeys.push(8); // Backspace
						var keyCode = e.which ? e.which : e.keyCode;
						var ret = ((keyCode >= 48 && keyCode <= 57) || specialKeys
								.indexOf(keyCode) != -1);
						document.getElementById("errorNumberSearch").style.display = ret ? "none"
								: "inline";
						return ret;
					};

					IsNumericPin = function(e) {
						var specialKeys = new Array();
						specialKeys.push(8); // Backspace
						var keyCode = e.which ? e.which : e.keyCode;
						var ret = ((keyCode >= 48 && keyCode <= 57) || specialKeys
								.indexOf(keyCode) != -1);
						document.getElementById("errorNumberPin").style.display = ret ? "none"
								: "inline";
						return ret;
					};

					IsEmail = function(val) {
						var atpos = val.indexOf("@");
						var dotpos = val.lastIndexOf(".");
						if (atpos < 1 || dotpos < atpos + 2
								|| dotpos + 2 >= val.length) {
							document.getElementById("errorEmail").style.display = "inline";
						} else {
							document.getElementById("errorEmail").style.display = "none";
						}
						return;
					};

				});