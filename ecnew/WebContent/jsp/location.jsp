<div>
	<div>
		<h1>Add a Location</h1>
		<div class="addDiv">

			<form id="addLocationForm">
				<div class="inputarea mandatorylabel">
					<label>Unique Code : <span>*</span></label><input type="text"
						name="warehouse" id="uniqueCode" /><span id="uniqueCodeError" class="error"></span>
				</div>

				<div class="inputarea mandatorylabel">
					<label class="mandatorylabel">Pincode <span>*</span></label><input style="display:inline"  type="text" name="pincode" size="20" onkeypress="return IsNumericPin(event);" id="pincode"
						ondrop="return false;" maxlength="6"/><span id="errorNumberPin" class="error"> * Input digits (0 - 9)</span> 
				</div>
				<div class="inputarea mandatorylabel">
					<label>Address : <span>*</span></label><input type="text" name="address" id="address" /><span id="addressError" class="error"></span>
				</div>

				<div class="inputarea mandatorylabel">
					<label>State : <span>*</span></label><input type="text" name="state" id="state"/><span id="stateError" class="error"></span>
				</div>

				<div class="inputarea mandatorylabel">
					<label>City : <span>*</span></label><input type="text" name="city" id="city"/><span id="cityError" class="error"></span>
				</div>
<!-- 
				<div class="inputarea">
					<label>Active : </label><select name="status">
						<option value="ACTIVE" title="ACTIVE">ACTIVE</option>
						<option value="INACTIVE" title="INACTIVE">INACTIVE</option>
					</select>
				</div> -->
				<div class="inputarea">
					<div>
						<input type="button" value="Submit" id="addLocationBtn" /> <input
							type="button" id="resetLocationBtn" value="Reset" />
					</div>
				</div>
				<div class="inputarea successLabel bigLabel">
					 <label id="successLabel" ></label>
				</div>
				<div id="locationPopuFail"></div>
			</form>
		</div>

	</div>
</div>
