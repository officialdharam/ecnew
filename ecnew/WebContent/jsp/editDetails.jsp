
<%
    java.util.Map<String, String> map = (java.util.Map<String, String>) request.getSession().getAttribute(
		    "warehouseDetails");
%>

<div>
	<div>
		<h1>Edit a Location</h1>
		<div class="addDiv">

			<form id="editWarehouseForm">
				<div id="locationDiv">
					<div class="inputarea">
						<label>Warehouse Name : </label><input type="text" value='<%=map.get("warehouse")%>' name="oldWHCode"/>
					</div>
					<div class="inputarea">
						<input type="hidden" name="warehouse"
							value="<%=map.get("warehouse")%>">
					</div>
					<div class="inputarea">
						<label>Postal Code :</label><input type="text" name="pincode"
							value="<%=map.get("pincode")%>">
					</div>
					<div class="inputarea">
						<label>Address : </label><input type="text" name="address"
							value="<%=map.get("address")%>">
					</div>

					<div class="inputarea">
						<label>State : </label><input type="text" name="state"
							value="<%=map.get("state")%>">
					</div>

					<div class="inputarea">
						<label>City : </label><input type="text" name="city"
							value="<%=map.get("city")%>">
					</div>

					<div class="inputarea">
						<label>Active : </label><select name="status">
							<option value="ACTIVE" title="ACTIVE">ACTIVE</option>
							<option value="INACTIVE" title="INACTIVE">INACTIVE</option>
						</select>
					</div>
					<div class="inputarea">
						<div>
							<input type="button" value="Submit" id="editDetailsBtn" /> <input
								type="button" id="resetDetailsBtn" value="Reset" />
						</div>
					</div>
				</div>
				<div class="inputarea bigLabel" id="msgDiv">
					<label id="successLabel"></label>
				</div>
				
			</form>
		</div>

	</div>
</div>
