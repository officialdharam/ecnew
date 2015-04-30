<script src="${pageContext.request.contextPath}/js/customerservice.js"
	type="text/javascript"></script>
<div id="cs-fragment-link">
	<h3>Book an Order</h3>
	<div id="orderFormDiv">
		<form id="orderForm">
			<div >
				<div class="orderFormBody">
					<div id="customerDetails" class="customerFormDetails">
						<h3>Customer Details</h3>
						<label class="mandatorylabel">Phone No. <span>*</span></label><input type="text" name="phoneNo" onkeypress="return IsNumeric(event);" ondrop="return false;" 
						id="phoneNumber" size="20" maxlength="10" /><span id="errorNumber" class="error"> * Input digits (0 - 9)</span> <br /> 
						
						<label >First Name</label><input type="text" name="firstName" size="20" onkeypress="return validFirstName(event);" 
						ondrop="return false;" 	id="firstName" size="20" placeholder="NO NAME"/><span id="errorFirstName" class="error"> * Characters only</span> <br /> 
						
						<label>Last	Name</label><input type="text" name="lastName" size="20" onkeypress="return validLastName(event);" ondrop="return false;" 
						id="lastName" size="20" /><span id="errorLastName" class="error"> * Characters only</span> <br /> 
						
						<label>Email </label><input type="text" name="email" size="20" onchange="return IsEmail(this.value)" id="emailId"/>
						<span id="errorEmail" class="error"> * Invalid Email</span> <br />
						<label class="mandatorylabel">Address <span>*</span></label> <textarea name="address" rows="4" cols="26" style="margin-left: 6px;" id="address"></textarea><span id="addressError" class="error"></span><br /> 
						<label class="mandatorylabel">Pincode <span>*</span></label><input	type="text" name="pincode" size="20" onkeypress="return IsNumericPin(event);" id="pincode"
						ondrop="return false;" maxlength="6"/><span id="errorNumberPin" class="error"> * Input digits (0 - 9)</span> <br /> 
					</div>
					<div id="orderDetails" class="orderFormDetails">
						<h3>Order Details</h3>
						<label  class="mandatorylabel">Pick Up Time: <span>*</span></label><input tabindex="-1"  type="text" name="pickUpTime" id="pickupTime" size="30" readonly="readonly" /><br />
						<span id="pickTimeError" class="error" style="min-height: 10px;" ></span> <br/>
						<label  class="mandatorylabel">Date: <span>*</span></label><input  tabindex="-1"  type="text" name="pickUpdate" id="date"><br /> 
						<span id="pickDateError" class="error"></span> <br/>
						<label>Comments:</label><textarea rows="4"  tabindex="-1"  name="splComments" cols="30"	style="margin-left: 6px;"></textarea>
					</div>
				</div>
				<div id="placeOrder">
					<input type="button" id="placeOrderBtn" value="Place Order" /> <label
						id="successLabel"></label>
				</div>
			</div>
		</form>
	</div>
	<h3>Search Customer Orders</h3>
	<div>
		<div id="customerInfoFormBody" class="customerInfoFormBody">
			<label class="mandatorylabel">Phone No. <span>*</span></label> <input type="text" name="phoneNo" id="phoneNumberSearch" onkeypress="return IsNumericSearch(event);" ondrop="return false;" 
						 size="20" maxlength="10" /><span id="errorNumberSearch" class="error"> * Input digits (0 - 9)</span> <br /><br /> 
			<input type="button" value="Search" id="searchOrdersForCustomer" />
			
		</div>
		<div style="margin-top: 10px;"><label id="failureLabel" ></label></div>
		<div id="customerOrdersDiv"></div>
	</div>
</div>