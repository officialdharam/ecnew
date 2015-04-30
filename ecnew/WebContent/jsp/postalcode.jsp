
<%
    java.util.List<String> centers = (java.util.List<String>) request.getSession().getAttribute("centers");
%>

<div id="leftmanage">
	<div>
		<h1>Select a center</h1>

		<div id="">
			<div class="labeltxt">Center :</div>

			<select name="warehouse" id="warehouseSelect">
				<%
				    for (String center : centers) {
				%>
				<option value=<%=center%> label="<%=center%>"><%=center%>
				</option>

				<%
				    }
				%>
			</select>
			<input type="button" value="Fetch Pincodes" id="fetchPincode" />

		</div>

	</div>
</div>
<div id="rightmanage" class="rightmanage">
	<div class="innermanage" id="pincodesinwarehouse"></div>
	
	<hr>

	<div class="innermanage" >
	
	</div>
	<div class="inputarea">
		<div>
			<input type="button" value="Fetch Pincodes" id="fetchPincode" />
		</div>
	</div>
</div>
