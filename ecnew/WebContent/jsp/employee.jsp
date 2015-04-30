<div>
	<div>
		<form id="addEmployeeForm">
			<h1>Add an Employee</h1>
			<div class="addDiv">

				<%
				    java.util.List<String> centers = (java.util.List<String>) request.getSession().getAttribute("centers");
				%>

				<div id="leftaddemployee">
					<div>
						<h1>Select a center</h1>
						<div id="selectcenterdiv">

							<div class="labeltxt">Center :</div>

							<select name="warehouse">
								<%
								    for (String center : centers) {
								%>
								<option value="<%=center%>" label="<%=center%>"><%=center%>
								</option>

								<%
								    }
								%>
							</select>
						</div>
					</div>
				</div>
				<div id="rightaddemployee">
					<div class="inputarea">
						<label>First Name : </label><input type="text" name="firstName" onkeypress="return validFirstName(event);" 
						ondrop="return false;" 	id="firstNameEmployee" size="20" /><span id="errorFirstName" class="error"> * Characters only</span>
					</div>

					<div class="inputarea">
						<label>Last Name : </label><input type="text" name="lastName" onkeypress="return validLastName(event);" ondrop="return false;" 
						id="lastNameEmployee" size="20" /><span id="errorLastName" class="error"> * Characters only</span>
					</div>

					<div class="inputarea">
						<label>Username : </label><input type="text" name="username" />
					</div>

					<div class="inputarea">
						<label>Email : </label><input type="text" name="email" />
					</div>

					<div class="inputarea">
						<label>Password : </label><input type="text" name="password" />
					</div>

					<div class="inputarea">
						<label>Active : </label><select name="status">
							<option value="ACTIVE" title="ACTIVE">ACTIVE</option>
							<option value="INACTIVE" title="INACTIVE">INACTIVE</option>
						</select>
					</div>
					<div id="addEmployeeBtnDiv" class="inputarea">
						<label> </label><input type="button" id="addEmployeeBtn"
							value="Add Employee" />
					</div>
					<div class="inputarea successLabel">
						<label id="successLabel"></label>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
