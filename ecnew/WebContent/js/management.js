/**
 * 
 */
$(document).ready(
				function() {

					$("#warehouseList").change(function() {
						var str = "";
						$("select option:selected").each(function() {
							str += $(this).text() + " ";
						});
						$("#currentWH").html(str);
						$(".fetchBtn").trigger('click');
						$(".respTxxt").hide();
					});

					$("#addLocationBtn").click(
							function() {
								
								if(validateLocationAdd()){
									
									var url = ctx + "/service/warehouse/add";
									
									// verify the form first
									var formData = JSON.stringify(jQuery(
									'#addLocationForm').serializeArray());
									
									makePOSTCall(url, formData, handleAddCenter);
								}else{
									$("#locationPopuFail").html("Please enter mandatory fields");
									$("#locationPopuFail").dialog({
										height : 100,
										width : 250,
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
									$("#locationPopuFail").dialog('open');
									setTimeout(function() {
										$("#locationPopuFail").dialog('close');
									}, 2000);
								}
							});

					// handler for place order call back
					handleAddCenter = function(data) {
						var message = data.statusMsg;
						var orderID = data.results[0];
						var msg = null;
						if (message === 'SUCCESS') {
							msg = "Center successfully added ";
							$("#successLabel").removeClass("failLabel");
							$("#successLabel").addClass("successLabel");
							$("#successLabel").text(msg);
						}else if(message == "CENTER_ALREADY_EXISTS"){
							msg = "Center already exists";
							$("#successLabel").removeClass("successLabel");
							$("#successLabel").addClass("failLabel");
							$("#successLabel").text(msg);
						}
						$('#resetLocationBtn').trigger("reset");
						
					};

					$("#asssignDevice").click(
							function() {
								var fe = $("#fes option:selected").val();
								var device = $("#devices option:selected")
										.text();
								var str = '{"fe":"' + fe + '","device":"'
										+ device + '"}';
								var url = ctx + '/service/device/assign';
								makePOSTCall(url, str, handleAssign);
							});

					handleAssign = function(data) {

						var message = data.statusMsg;
						if (message === 'SUCCESS') {
							summary = "Assignment successful.";
						} else {
							summary = "Assignment of device failed.";
						}

						$("#deassignmentMessage").html(summary);
						$("#deassignmentMessage").dialog({
							height : 100,
							width : 200,
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
						$("#deassignmentMessage").dialog('open');
						setTimeout(function() {
							$("#deassignmentMessage").dialog('close');
							location.reload(true);
						}, 2000);
					};

					$("#editDetailsBtn")
							.click(
									function() {
										var url = ctx
												+ "/service/warehouse/editDetails";

										// verify the form first
										var formData = JSON.stringify(jQuery(
												'#editWarehouseForm')
												.serializeArray());

										makePOSTCall(url, formData,
												editDetailsHandler);
									});

					editDetailsHandler = function() {
						$("#successLabel").html(
								"Warehouse details successfully edited.");
					};

					$("#resetDetailsBtn")
							.click(
									function() {

										// reset form
										$("#editWarehouseForm")
												.find(
														'input:text, input:password, input:file, select, textarea')
												.val('');
										$("#editWarehouseForm").find(
												'input:radio, input:checkbox')
												.removeAttr('checked')
												.removeAttr('selected');
									});

					resetForm = function(selector) {
						$('"#' + selector + '"')
								.find(
										'input:text, input:password, input:file, select, textarea')
								.val('');
					};

					/**
					 * *******************************************************************
					 * Pincode Management Starts here
					 * *******************************************************************
					 */

					/** 1. Pincode fetch and show * */
					$("#fetchPincodes")
							.click(
									function() {
										warehouseName = $(
												"#warehouseList option:selected")
												.text();
										console.log(warehouseName);
										var url = ctx
												+ "/service/warehouse/fetchPincodes?warehouseName="
												+ warehouseName;
										makeGETCall(url, handlePincodes);
									});

					handlePincodes = function(data) {
						var message = data.statusMsg;
						var responseArray = data.results;
						if (message === 'SUCCESS') {

							var tableStr = '';
							$
									.each(
											responseArray,
											function(index, value) {
												tableStr = tableStr
														.concat('<div class="radioGroup"><input type="radio" name="pincodes" value="'
																+ value
																+ '">'
																+ value
																+ '</div>');

											});
							$("#pincodesinwarehouse").html(tableStr);
						}
					};

					/** 2. Add pincode click * */
					$("#addPincode")
							.click(
									function() {
										var warehouseName = $(
												"#warehouseList option:selected")
												.text();
										var summary = '<form id="addPinForm" method="post" action="'
												+ ctx
												+ '/service/warehouse/addPincode"><div class="inputarea" align="center" ><label class="mandatorylabel">Pin code <span>*</span></label><input type="text" name="addedPincode" id="pincode" onkeypress="return IsNumericPin(event);" id="pincode" '
												+ 'ondrop="return false;" maxlength="6"/><br/><span id="errorNumberPin" class="error"> * Input digits (0 - 9)</span> <input type="hidden" name="warehouse" value="'
												+ warehouseName
												+ '"/><br/><input type="button" value="Add" id="addPinBtn" onclick="formSubmit()"/></div></form>';

										$("#popupManagePincodes").html(summary);
										$("#popupManagePincodes").dialog({
											height : 200,
											width : 400,
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
										$("#popupManagePincodes")
												.dialog('open');
									});

					/** Delete Pincode click * */
					$("#deletePincode")
							.click(
									function() {
										var warehouseName = $(
												"#warehouseList option:selected")
												.text();
										var pincode = $(
												"input[name=pincodes]:checked")
												.val();
										if(pincode === undefined || pincode === null || pincode.length <=0){
											var summary = '<font color="red"><h3>Please select pincode for deletion</h3></font>';
											$("#deletionSuccessPopup").html(summary);
											$("#deletionSuccessPopup").dialog({
												height : 200,
												width : 300,
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
												}
											});

											$("#deletionSuccessPopup").dialog('open');
											$("#fetchDevices").trigger('click');
											setTimeout(function() {
												$("#deletionSuccessPopup").dialog('close');
											}, 2000);
										}else{
											var url = ctx
													+ "/service/warehouse/deletePincode?warehouseName="
													+ warehouseName + "&pincode="
													+ pincode;
											makeGETCall(url, deletePincodeHandler);
										}
									});

					deletePincodeHandler = function(data) {
						var summary = '';
						if (data === 'true' || data === true) {
							summary = '<font color="green"><h3>Deletion successful</h3></font>';
						} else {
							summary = '<font color="red"><h3>Deletion failed</h3></font>';
						}

						$("#deletionSuccessPopup").html(summary);
						$("#deletionSuccessPopup").dialog({
							height : 100,
							width : 200,
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

						$("#deletionSuccessPopup").dialog('open');
						$("#fetchPincodes").trigger('click');
						setTimeout(function() {
							$("#deletionSuccessPopup").dialog('close');
						}, 2000);
					};

					/** 3. Transfer Pincode click * */
					$("#transferPincode").click(function() {
						var url = ctx + "/service/fetchCenters?active=ACTIVE";
						makeGETCall(url, handleTransferPins);
					});

					handleTransferPins = function(data) {
						var message = data.statusMsg;
						if (message === 'SUCCESS') {

							var tableStr = '<select name="whNew">';
							$.each(data.results, function(index, value) {
								tableStr = tableStr.concat('<option label="'
										+ value + '" value="' + value + '">'
										+ value + '</option>');

							});

							tableStr = tableStr.concat('</select>');
							var warehouseName = $(
									"#warehouseList option:selected").text();

							var pincode = $("input[name=pincodes]:checked")
									.val();

							if (pincode === undefined) {
								var summary = '<h3>Please fetch pincodes and select one.</h3>';
								$("#deletionSuccessPopup").html(summary);
								$("#deletionSuccessPopup").dialog({
									height : 'auto',
									width : 300,
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
									}
								});
								$("#deletionSuccessPopup").dialog('open');

							} else {

								var summary = '<form id="addPinForm" method="post" action="'
										+ ctx
										+ '/service/warehouse/transferPincode"><div class="inputarea" align="center" ><label>Select Warehouse</label><input type="hidden" name="pincode" value="'
										+ pincode
										+ '"/><input type="hidden" name="warehouse" value="'
										+ warehouseName
										+ '" />'
										+ tableStr
										+ '<input type="submit" value="Transfer" id="transferPinBtn"/> </div></form>';

								$("#popupManagePincodes").html(summary);
								$("#popupManagePincodes").dialog({
									height : 250,
									width : 300,
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
									}
								});
								$("#popupManagePincodes").dialog('open');
							}
						}
					};

					/**
					 * ************************************************************
					 * Pincode Management Ends here
					 * ************************************************************
					 */

					/**
					 * ************************************************************
					 * Device Management Starts here
					 * ************************************************************
					 */

					/** 1. Fetch devices * */
					$("#fetchDevices")
							.click(
									function() {
										warehouseName = $(
												"#warehouseList option:selected")
												.text();
										console.log(warehouseName);
										var url = ctx
												+ "/service/warehouse/fetchDevices?warehouseName="
												+ warehouseName;
										makeGETCall(url, handleDevices);
									});

					handleDevices = function(data) {
						var message = data.statusMsg;
						var responseArray = data.results;
						if (message === 'SUCCESS') {

							var tableStr = '';
							$
									.each(
											responseArray,
											function(index, value) {
												tableStr = tableStr
														.concat('<div class="radioGroup"><input type="radio" name="devices" value="'
																+ value
																+ '">'
																+ value
																+ '</div>');

											});
							$("#devicesinwarehouse").html(tableStr);
						}
					};

					/** 2. Add devices * */
					$("#addDevice")
							.click(
									function() {
										var warehouseName = $(
												"#warehouseList option:selected")
												.text();
										var summary = '<form id="addDeviceForm" method="post" action="'
												+ ctx
												+ '/service/warehouse/addDevice"><div class="inputarea" ><label  class="mandatorylabel">Device ID <span>*</span></label><input type="text" name="addedDevice" id="addedDevice" /></div><input type="hidden" name="warehouse" value="'
												+ warehouseName
												+ '"/><div class="inputarea"><label>Status</label><select name="deviceStatus" id="deviceIDSelect"><option value="ACTIVE">ACTIVE</option><option value="INACTIVE">INACTIVE</option></select></div><span id="errorDeviceName" class="error"> * Device Name Required</span></div><div class="inputarea" align="center"><input type="button" value="Add" id="addDeviceBtn" onclick="addDeviceSubmit();"/></div></form>';

										$("#popupManageDevices").html(summary);
										$("#popupManageDevices").dialog({
											height : 230,
											width : 400,
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
										$("#popupManageDevices").dialog('open');
									});

					/** Delete device click * */
					$("#deleteDevice")
							.click(
									function() {
										var warehouseName = $(
												"#warehouseList option:selected")
												.text();
										var device = $(
												"input[name=devices]:checked").val();
												
												if(device === undefined || device === null || device.length <=0){
													var summary = '<font color="red"><h3>Please select device for deletion</h3></font>';
													$("#deletionSuccessPopup").html(summary);
													$("#deletionSuccessPopup").dialog({
														height : 200,
														width : 300,
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

													$("#deletionSuccessPopup").dialog('open');
													$("#fetchDevices").trigger('click');
													setTimeout(function() {
														$("#deletionSuccessPopup").dialog('close');
													}, 2000);
												}else{
													var url = ctx
													+ "/service/warehouse/deleteDevice?warehouseName="
													+ warehouseName + "&device="
													+ device;
													makeGETCall(url, deleteDeviceHandler);
												}
												
										

									});

					deleteDeviceHandler = function(data) {
						var summary = '';
						if (data === 'true' || data === true) {
							summary = '<font color="green"><h3>Deletion successful</h3></font>';
						} else {
							summary = '<font color="red"><h3>Deletion failed</h3></font>';
						}

						$("#deletionSuccessPopup").html(summary);
						$("#deletionSuccessPopup").dialog({
							height : 100,
							width : 200,
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

						$("#deletionSuccessPopup").dialog('open');
						$("#fetchDevices").trigger('click');
						setTimeout(function() {
							$("#deletionSuccessPopup").dialog('close');
						}, 2000);
					};

					/** Transfer Device * */
					$("#transferDevice").click(function() {
						var url = ctx + "/service/fetchCenters?active=ACTIVE";
						makeGETCall(url, handleTransferDevices);
					});

					handleTransferDevices = function(data) {
						var message = data.statusMsg;
						if (message === 'SUCCESS') {

							var tableStr = '<select name="whNew">';
							$.each(data.results, function(index, value) {
								tableStr = tableStr.concat('<option label="'
										+ value + '" value="' + value + '">'
										+ value + '</option>');

							});

							tableStr = tableStr.concat('</select>');
							var warehouseName = $(
									"#warehouseList option:selected").text();

							var device = $("input[name=devices]:checked").val();

							if (device === undefined) {
								var summary = '<h3>Please fetch devices and select one.</h3>';
								$("#deletionSuccessPopup").html(summary);
								$("#deletionSuccessPopup").dialog({
									height : 'auto',
									width : 300,
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
									}
								});
								$("#deletionSuccessPopup").dialog('open');

							} else {

								var summary = '<form id="addDeviceForm" method="post" action="'
										+ ctx
										+ '/service/warehouse/transferDevice"><div class="inputarea" align="center" ><label>Select Warehouse</label><input type="hidden" name="device" value="'
										+ device
										+ '"/><input type="hidden" name="warehouse" value="'
										+ warehouseName
										+ '" />'
										+ tableStr
										+ '<input type="submit" value="Transfer" id="transferDeviceBtn"/> </div></form>';

								$("#popupManageDevices").html(summary);
								$("#popupManageDevices").dialog({
									height : 250,
									width : 300,
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
									}
								});
								$("#popupManageDevices").dialog('open');
							}
						}
					};

					/**
					 * ********************************************************************
					 * Device Management Ends here
					 * ********************************************************************
					 */

					/**
					 * ************************************************************
					 * Field Executives Management Starts here
					 * ************************************************************
					 */

					/** 1. Fetch field executives click * */
					$("#fetchExecutives")
							.click(
									function() {
										warehouseName = $(
												"#warehouseList option:selected")
												.text();
										console.log(warehouseName);
										var url = ctx
												+ "/service/warehouse/fetchExecutives?warehouseName="
												+ warehouseName;
										makeGETCall(url, handleExecutives);
									});

					handleExecutives = function(data) {
						var message = data.statusMsg;
						var responseArray = data.results;
						if (message === 'SUCCESS') {

							var tableStr = '';
							$
									.each(
											responseArray,
											function(index, value) {
												tableStr = tableStr
														.concat('<div class="radioGroup"><input type="radio" name="executives" value="'
																+ value
																+ '">'
																+ value
																+ '</div>');

											});
							$("#feinwarehouse").html(tableStr);
						}
					};

					/** 2. Add field executives * */
					$("#addFE")
							.click(
									function() {
										var warehouseName = $(
												"#warehouseList option:selected")
												.text();
										var summary = '<form id="addFEForm" method="post" action="'
												+ ctx
												+ '/service/warehouse/addFE"><div class="inputarea" ><label class="mandatorylabel">Executive Name <span>*</span></label><input type="text" name="addedFE" id="addedFE"/></div><input type="hidden" name="warehouse" value="'
												+ warehouseName
												+ '"/><div class="inputarea"><label>Status</label><select name="feStatus" id="feStatus"><option value="ACTIVE">ACTIVE</option><option value="INACTIVE">INACTIVE</option></select></div><br/><div><span id="errorFEName" class="error"> * Executive Name Required</span></div><br/><div class="inputarea" align="center"><input type="button" value="Add" id="addFEBtn" onClick="addFESubmit()"/></div></form>';

										$("#popupManageFE").html(summary);
										$("#popupManageFE").dialog({
											height : 260,
											width : 400,
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
										$("#popupManageFE").dialog('open');
									});

					/** Delete device click * */
					$("#deleteFE")
							.click(
									function() {
										var warehouseName = $(
												"#warehouseList option:selected")
												.text();
										var executive = $(
												"input[name=executives]:checked")
												.val();
										
										if(executive === undefined || executive === null || executive.length <=0){
											var summary = '<font color="red"><h3>Please select executive for deletion</h3></font>';
											$("#deletionSuccessPopup").html(summary);
											$("#deletionSuccessPopup").dialog({
												height : 200,
												width : 300,
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

											$("#deletionSuccessPopup").dialog('open');
											$("#fetchDevices").trigger('click');
											setTimeout(function() {
												$("#deletionSuccessPopup").dialog('close');
											}, 2000);
										}else{
										var url = ctx
												+ "/service/warehouse/deleteFE?warehouseName="
												+ warehouseName
												+ "&executives=" + executive;
										makeGETCall(url, deleteFEHandler);
										}

									});

					deleteFEHandler = function(data) {
						var summary = '';
						if (data === 'true' || data === true) {
							summary = '<font color="green"><h3>Deletion successful</h3></font>';
						} else {
							summary = '<font color="red"><h3>Deletion failed</h3></font>';
						}

						$("#deletionSuccessPopup").html(summary);
						$("#deletionSuccessPopup").dialog({
							height : 100,
							width : 200,
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

						$("#deletionSuccessPopup").dialog('open');
						$("#fetchExecutives").trigger('click');
						setTimeout(function() {
							$("#deletionSuccessPopup").dialog('close');
						}, 2000);
					};

					/** Transfer Device * */
					$("#transferFE").click(function() {
						var url = ctx + "/service/fetchCenters?active=ACTIVE";
						makeGETCall(url, handleTransferExecutives);
					});

					handleTransferExecutives = function(data) {
						var message = data.statusMsg;
						if (message === 'SUCCESS') {

							var tableStr = '<select name="whNew">';
							$.each(data.results, function(index, value) {
								tableStr = tableStr.concat('<option label="'
										+ value + '" value="' + value + '">'
										+ value + '</option>');

							});

							tableStr = tableStr.concat('</select>');
							var warehouseName = $(
									"#warehouseList option:selected").text();

							var executive = $("input[name=executives]:checked")
									.val();

							if (executive === undefined) {
								var summary = '<h3>Please fetch executives and select one.</h3>';
								$("#deletionSuccessPopup").html(summary);
								$("#deletionSuccessPopup").dialog({
									height : 'auto',
									width : 300,
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
									}
								});
								$("#deletionSuccessPopup").dialog('open');

							} else {

								var summary = '<form id="transferFEForm" method="post" action="'
										+ ctx
										+ '/service/warehouse/transferFE"><div class="inputarea" align="center" ><label>Select Warehouse</label><input type="hidden" name="fe" value="'
										+ executive
										+ '"/><input type="hidden" name="warehouse" value="'
										+ warehouseName
										+ '" />'
										+ tableStr
										+ '<input type="submit" value="Transfer" id="transferFEBtn"/> </div></form>';

								$("#popupManageFE").html(summary);
								$("#popupManageFE").dialog({
									height : 250,
									width : 300,
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
									}
								});
								$("#popupManageFE").dialog('open');
							}
						}
					};

					/**
					 * ********************************************************************
					 * Field Executive Management Ends here
					 * ********************************************************************
					 */

					$("#asssignedDevice")
							.click(
									function() {
										var warehouse = $("#currentCenterName")
												.text();
										var url = ctx
												+ "/service/warehouse/deviceAssignments?warehouseName="
												+ warehouse;
										makeGETCall(url, handleAssignedDevices,
												null);
									});

					handleAssignedDevices = function(data) {

						var message = data.statusMsg;
						var responseArray = data.results;
						if (message === 'SUCCESS') {
							if (responseArray === null
									|| responseArray.length === 0) {
								$("#assignedDevices")
										.html(
												"<h3>No devices assigned in this Center.");
							} else {
								var tableStr = '<table><thead><th>DEVICE CODE</th><th>FIELD EXECUTIVE</th><th>ACTION</th></thead><tbody>';
								$
										.each(
												responseArray,
												function(index, value) {
													tableStr = tableStr
															.concat('<tr>');
													tableStr = tableStr
															.concat('<td align="center">');
													tableStr = tableStr
															.concat(value.device);
													tableStr = tableStr
															.concat('</td>');
													tableStr = tableStr
															.concat('<td align="center">');
													tableStr = tableStr
															.concat(value.name);
													tableStr = tableStr
															.concat('</td>');
													tableStr = tableStr
															.concat('<td align="center">');
													tableStr = tableStr
															.concat('<input type="button" value="Unassign" style="margin-bottom: 5px;margin-top:0px;"  onclick="handleDeAssign(');
													tableStr = tableStr
															.concat(value.assignmentID
																	+ ','
																	+ value.deviceID
																	+ ','
																	+ value.fe);
													tableStr = tableStr
															.concat(')">');
													tableStr = tableStr
															.concat('</td>');
													tableStr = tableStr
															.concat('</tr>');

												});
								$("#assignedDevices").html(tableStr);

							}

						}

					};

					handleDeAssign = function(id, deviceID, userID) {
						var url = ctx + "/service/warehouse/deassign";

						var data = '{"fe":"' + userID + '","deviceID":"'
								+ deviceID + '","assignmentID":"' + id + '"}';
						makePOSTCall(url, data, handleDeAssignResponse);
					};

					handleDeAssignResponse = function(data) {

						var message = data.statusMsg;
						if (message === 'SUCCESS') {
							summary = "Unassignment successful.";
						} else {
							summary = "Unassignment of device failed.";
						}

						$("#deassignmentMessage").html(summary);
						$("#deassignmentMessage").dialog({
							height : 100,
							width : 200,
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
						$("#deassignmentMessage").dialog('open');
						setTimeout(function() {
							$("#deassignmentMessage").dialog('close');
							location.reload(true);
						}, 2000);

					};

					$("#resetLocationBtn").click(function() {
						$("#addLocationForm").get(0).reset();
					});

					validAddLocation = function() {
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

					formSubmit = function() {
						if (validatePinCode()) {
							$("#addPinForm").submit();
						}
					};

					validatePinCode = function() {
						var status = true;
						var pincode = $("#pincode").val();
						if (pincode === undefined || pincode === null
								|| pincode.length < 6) {
							$("#errorNumberPin").text("6 digits required.");
							$("#errorNumberPin").css("display", "inline");
							status = false;
						} else {
							$("#errorNumberPin").css("display", "none");
						}
						return status;
					};

					addFESubmit = function() {
						if (validateFE()) {
							$("#addFEForm").submit();
						}
					};
					
					validateFE = function() {
						var status = true;
						var addedFE = $("#addedFE").val();
						if (addedFE === undefined || addedFE === null
								|| addedFE.length <= 0) {
							$("#errorFEName").css("display", "inline");
							status = false;
						} else {
							$("#errorFEName").css("display", "none");
						}
						return status;
					};
					
					addDeviceSubmit = function() {
						if (validateDevice()) {
							$("#addDeviceForm").submit();
						}
					};
					
					validateDevice = function() {
						var status = true;
						var addedDevice = $("#addedDevice").val();
						if (addedDevice === undefined || addedDevice === null
								|| addedDevice.length <= 0) {
							$("#errorDeviceName").css("display", "inline");
							status = false;
						} else {
							$("#errorDeviceName").css("display", "none");
						}
						return status;
					};


					validateLocationAdd = function(){
						var status = true;
						var code = $("#uniqueCode").val();
						var pincode = $("#pincode").val();
						var address = $("#address").val();
						var state = $("#state").val();
						var city = $("#city").val();
						if(code === undefined || code === null || code.length <= 0){
							status = false;
						}else{
						}
						
						if(pincode === undefined || pincode === null || pincode.length < 6){
							status = false;
						}else{
						}
						
						if(address === undefined || address === null || address.length === 0){
							status = false;
						}else{
						}
						
						if(state === undefined || state === null || state.length === 0){
							status = false;
						}else{
						}
						
						if(city === undefined || city === null || city.length === 0){
							status = false;
						}else{
							
						}
						
						return status;
					};
					

				});
