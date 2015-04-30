$(document)
		.ready(

				function() {

					$("#wh-fragment-link").accordion({
						heightStyle : 'panel',
						collapsible : 'true',
						active : 'true',
						active : 0
					});

					handleReconcileCallback = function(data, selector) {
						var message = data.statusMsg;
						var responseArray = data.results;
						var tableStr = "";
						if (message === 'SUCCESS') {
							tableStr = tableStr.concat('<table border="1">');
							tableStr = tableStr.concat('<thead>');
							tableStr = tableStr
									.concat('<th colspan="2">Dashboard</th>');
							tableStr = tableStr.concat('</thead>');
							tableStr = tableStr.concat('<tbody>');
							$.each(responseArray, function(index, value) {
								tableStr = tableStr.concat('<tr>');
								tableStr = tableStr.concat('<td>' + value.name
										+ '</td>');
								tableStr = tableStr.concat('<td>' + value.value
										+ '</td>');
								tableStr = tableStr.concat('</tr>');
							});

							tableStr = tableStr.concat('</table>');
							$(selector).html(tableStr);
							return tableStr;
						}

					};

					// when the in-bound reconciliation accordion is activated
					fetchInbound = function() {
						// submit using AJAX.
						var url = ctx + "/service/warehouse/reconcile/inbound";
						makeGETCall(url, handleReconcileCallback,
								"#reconcileInbound");
					};

					// when the in-bound reconciliation accordion is activated
					fetchOutbound = function() {
						// submit using AJAX.
						var url = ctx + "/service/warehouse/reconcile/outbound";
						makeGETCall(url, handleReconcileCallback,
								"#reconcileOutbound");
					};

					// handler for accordion header clicks. To get picked
					// orders.
					$("#reconcileOutboundHead").click(function() {
						fetchOutbound();
					});

					fetchInbound();

					// fetch orders picked up by the device.
					$("#searchOrdersForDevice").click(
							function() {
								var deviceID = $("#deciveIDNo").val();
								var url = ctx + "/service/warehouse/orders/"
										+ deviceID;
								makeGETCall(url, handleDeviceOrders);
							});

					// handler for device orders call back.
					handleDeviceOrders = function(data) {
						var message = data.statusMsg;
						var responseArray = data.results;
						var tableStr = "";
						if (message === 'SUCCESS') {

							$
									.each(
											responseArray,
											function(index, value) {
												tableStr = tableStr
														.concat('<h3><a href="#" onClick="handleOrderIDClick('
																+ value.orderID
																+ ')">Order ID : '
																+ value.orderDisplay
																+ '</h3>');
												tableStr = tableStr
														.concat(prepareOrderContentTable(value));
											});

							// add the table to the picked orders div.
							$("#deviceOrdersDiv").html(tableStr);
						} else {
							$("#deviceOrdersDiv")
									.html(
											"<h2 style='display:inline;'>Device not found<h2>");
						}

					};

					$("#buildLoadBtn").click(
							function() {
								var url = ctx + "/service/warehouse/buildload";

								// verify the form first
								var formData = JSON.stringify(jQuery(
										'#loadForm').serializeArray());

								makePOSTCall(url, formData, handleLoad);
							});

					handleLoad = function(data) {
						var msg = '';

						var message = data.statusMsg;
						if (message === 'SUCCESS') {
							msg = "<h2>Load successfully built.</h2>";
						} else {
							msg = "<h2>Failed to build load.</h2>";
						}

						$("#messageLoad").html(msg);
						setTimeout(function() {
							location.reload(true);
						}, 2000);

					};

				});
