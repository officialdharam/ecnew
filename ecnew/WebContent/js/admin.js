/**
 * 
 */
$(document)
		.ready(

				function() {
					
					var multiSelectedStr=[];

					$("#addUser")
							.click(
									function() {
										var warehouse = $("#currentCenterName")
												.text();
										$("#users")
												.html(
														'<div style="min-height:420px;"><form id="addEmployeeForm"><h1>Add an Employee</h1> <span id="successMsg" style="display:none">Success</span><div class="addDiv"><div id="rightaddemployee"><div class="inputarea"><label class="mandatorylabel">First Name : <span>*</span>'
																+ '</label><input type="text" name="firstName" onkeypress="return validFirstName(event);"  ondrop="return false;"  id="firstNameEmployee" size="20" />'
																+ '<span id="errorFirstName" class="error"> * Characters only</span></div><div class="inputarea"><label>Last Name : </label><input type="text" '
																+ 'name="lastName"  onkeypress="return validLastName(event);"  ondrop="return false;"  id="lastNameEmployee" size="20" /><span id="errorLastName" class="error">'
																+ ' * Characters only</span></div><div class="inputarea"><label class="mandatorylabel">Username : <span>*</span></label><input type="text" name="username" id="usernameEmployee"/><span id="errorUsername" class="error"></div><div class="inputarea"><label>Email : </label>'
																+ '<input type="text" name="email" onchange="return IsEmail(this.value)"/><span id="errorEmail" class="error"> * Invalid Email</span></div><div class="inputarea">'
																+ '<label class="mandatorylabel">Password : <span>*</span></label><input type="text" name="password" id="passwordEmployee"/><span id="errorPassword" class="error"> * Characters only</span><input type="hidden" name="warehouse" value ="'
																+ warehouse
																+ '" /></div><div class="inputarea">'
																+ '<label>Active : </label><select name="status"><option value="ACTIVE" title="ACTIVE">ACTIVE</option><option value="INACTIVE" title="INACTIVE">INACTIVE</option></select></div>'
																+ '<div class="inputarea"><label class="mandatorylabel">Group Name : <span>*</span></label>'
																+ fetchGroupString()
																+ '</div>'
																+ '</div><div id="addEmployeeBtnDiv" class="inputarea"><label> </label><input type="button" id="addEmployeeBtn" onCLick="addEmployee()" value="Add Employee" /></div>'
																+ '</div></div></form></div>');
									});

					$("#fetchUser").click(function() {
						var url = ctx + "/service/user/fetch";
						makeGETCall(url, handleFetchUsers, null);
					});

					handleFetchUsers = function(data) {
						var message = data.statusMsg;
						var responseArray = data.results;
						if (message === 'SUCCESS') {
							var tableStr = '<table><thead><th>USERNAME</th><th>NAME</th><th>GROUP</th><th>ACTION</th></thead><tbody>';
							$
									.each(
											responseArray,
											function(index, value) {
												tableStr = tableStr
														.concat('<tr>');
												tableStr = tableStr
														.concat('<td align="center">');
												tableStr = tableStr
														.concat(value.userName);
												tableStr = tableStr
														.concat('</td>');
												tableStr = tableStr
														.concat('<td align="center">');
												tableStr = tableStr
														.concat(value.fName
																+ ' '
																+ value.lName);
												tableStr = tableStr
														.concat('</td>');
												tableStr = tableStr
														.concat('<td align="center">');
												tableStr = tableStr
														.concat(value.groupName);
												tableStr = tableStr
														.concat('</td>');
												tableStr = tableStr
														.concat('<td align="center">');
												tableStr = tableStr
														.concat('<input type="button" value="Edit" style="margin-bottom: 5px;margin-top:0px;"  onclick="handleUserEdit(\'');
												tableStr = tableStr
														.concat(value.userName
																+ '\',\''
																+ value.fName
																+ '\',\''
																+ value.lName
																+ '\',\''
																+ value.groupName);
												tableStr = tableStr
														.concat('\')">');
												tableStr = tableStr
														.concat('</td>');
												tableStr = tableStr
														.concat('</tr>');

											});
							$("#users").html(tableStr);

						} else {

						}
					};

					fetchCentersString = function() {
						var centerArray = getArray(centers);
						var table = '<select name="centerName" id="selectCentersId">';
						for (var i = 0; i < centerArray.length; i++) {
							if(i == 0){
								multiSelectedStr.push(centerArray[i]);
							}
							table = table.concat('<option value="'
									+ centerArray[i].trim() + '" label="'
									+ centerArray[i].trim() + '" ');
							table = table.concat('>' + centerArray[i].trim()
									+ '</option>');
						}
						table = table.concat('</select>');
						
						return table;

					};

					handleUserEdit = function(userName, firstName, lastName,
							groupName) {
						var tableStr = '<div><form id="updateEmployeeForm"><h1 style="text-align:center">Edit Employee</h1><div class="addDiv"><div id="rightaddemployee"><div class="inputarea">'
								+ '<label>First Name : </label><input type="text" name="firstName" style="width: 217px;" value="'
								+ firstName
								+ '"/></div><div class="inputarea"><label>Last Name : '
								+ '</label><input type="text" style="width: 217px;" name="lastName" value="'
								+ lastName
								+ '"/></div><div class="inputarea"><label>Username : </label>'
								+ '<input type="text" style="width: 217px;" name="username" value="'
								+ userName
								+ '" readonly/></div><div class="inputarea"><label>Active : </label><select  style="width: 231px;" name="status">'
								+ '<option value="ACTIVE" title="ACTIVE">ACTIVE</option><option value="INACTIVE" title="INACTIVE">INACTIVE</option></select></div><div class="inputarea">'
								+ '<label class="mandatorylabel">Group Name : <span>*</span></label>'
								+ fetchGroupString(groupName)
								+ '</div>'
								+ '<div class="inputarea"><label class="mandatorylabel">Select Centers : <span>*</span></label>'
								+ fetchCentersString()
								+ '</div>'
								+ '<div class="inputarea successLabel"><label id="successLabel"></label></div></div></div></form></div>';
						$("#users").html(tableStr);
						$("#selectCentersId").multiselect(
								{
									click : function(event, ui) {
										var status = ui.checked ? 'checked' : 'unchecked';
										if(status === 'checked'){
											multiSelectedStr.push(ui.value);
										}else{
											multiSelectedStr.pop(ui.value);
										}
									}
								});

						$("#editEmployeeBtn").css("display", "inline");
					};

					addEmployee = function() {

						if (validateAddEmployee()) {
							var url = ctx + "/service/user/add";

							// verify the form first
							var formData = JSON.stringify(jQuery(
									'#addEmployeeForm').serializeArray());

							makePOSTCall(url, formData, handleAddEmployee);
						}

					};

					// handler for place order call back
					handleAddEmployee = function(data) {
						var message = data.statusMsg;
						var orderID = data.results[0];
						var msg = null;
						if (message === 'SUCCESS') {
							msg = "Employee successfully added ";
						}
						$('#addEmployeeBtn').trigger("reset");
						$("#successMsg").text(msg);
						$("#successMsg").css("display", "inline");
					};

					$("#editEmployeeBtn").click(function() {
						updateEmployee();
					});

					updateEmployee = function() {
						var url = ctx + "/service/user/update";
						
						var str = '';
						for(var i = 0 ; i < multiSelectedStr.length ;i++){
							if(i > 0 )
								str = str.concat(' ');
							str = str.concat(multiSelectedStr[i]);
						};
						
						var formDataArray = jQuery('#updateEmployeeForm').serializeArray();
						formDataArray.push({"name":"centers", "value":str});
						// verify the form first
						var formData = JSON.stringify(formDataArray);
						
						makePOSTCall(url, formData, handleUpdateEmployee);
					};

					// handler for place order call back
					handleUpdateEmployee = function(data) {
						var message = data.statusMsg;
						var msg = null;
						if (message === 'SUCCESS') {
							msg = "Employee successfully updated ";
						} else {
							msg = "Failed to update employee. Please try again later.";
						}
						$("#successLabel").text(msg);
					};

					fetchGroupString = function(selected) {
						var table = '<select name="groupName" style="width: 231px;">';
						var array = getArray(groups);
						for (var i = 0; i < array.length; i++) {
							table = table.concat('<option value="'
									+ array[i].trim() + '" label="'
									+ array[i].trim() + '" ');
							if (selected != undefined && selected === array[i]) {
								table.concat('selected');
							}

							table = table.concat('>' + array[i].trim()
									+ '</option>');
						}
						;
						table = table.concat('</select>');
						return table;
					};

					getArray = function(groups) {
						var str = groups.substr(1, groups.length - 2);
						var array = str.split(',');
						return array;
					};

					validateAddEmployee = function() {
						var status = true;
						var firstName = $("#firstNameEmployee").val();
						var username = $("#usernameEmployee").val();
						var password = $("#passwordEmployee").val();
						if (firstName === undefined || firstName === null
								|| firstName.length === 0) {
							$("#errorFirstName").text("Name required.");
							$("#errorFirstName").css("display", "inline");
							status = false;
						} else {
							$("#errorFirstName").css("display", "none");
						}

						if (username === undefined || username === null
								|| username.length === 0) {
							$("#errorUsername").text("Username required.");
							$("#errorUsername").css("display", "inline");
							status = false;
						} else {
							$("#errorUsername").css("display", "none");
						}

						if (password === undefined || password === null
								|| password.length === 0) {
							$("#errorPassword").text("Password required.");
							$("#errorPassword").css("display", "inline");
							status = false;
						} else {
							$("#errorPassword").css("display", "none");
						}

						return status;
					};
				});
