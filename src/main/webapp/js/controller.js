$(document).ready(function(){
	documentBaseUrl = "http://" + document.location.host + document.location.pathname;

	// laod radio channels
	controller.loadRadioChanels(documentBaseUrl);
	
	controller.getCurrent();
	
	// get root files
	controller.getFiles("");
	controller.getStations();
	
	controller.radioChanels = [];
	
	
	$("#tabLocal").click(function(){
		$("#tabLocal").parent().addClass("selected");
		$("#tabRadio").parent().removeClass("selected");
		$("#tabYoutube").parent().removeClass("selected");
		$("#tabLocalContent").css("display","block");
		$("#tabRadioContent").css("display","none")
		$("#tabYoutubeContent").css("display","none")
	});
	$("#tabRadio").click(function(){
		$("#tabRadio").parent().addClass("selected")
		$("#tabLocal").parent().removeClass("selected")
		$("#tabYoutube").parent().removeClass("selected")
		$("#tabLocalContent").css("display","none")
		$("#tabYoutubeContent").css("display","none")
		$("#tabRadioContent").css("display","block")
	});
	$("#youtubeIconTab").click(function(){
		controller.addYoutube();
	});
	
	$("#current").click(function(){
		controller.getCurrent();
	});
	$("#controllJumpLeft").click(function(){
		controller.prev();
	});
	$("#controllSeekLeft").click(function(){
		var target = Number(controller.actualSongPercent)-10 //seek left 10%
		if(target < 0){
			target = 0;
		}
		controller.seekTo(target);
	});
	$("#controllStop").click(function(){
		controller.stop();
	});
	$("#controllPlayPause").click(function(){
		controller.togglePause();
	});
	$("#controllSeekRight").click(function(){
		var target = Number(controller.actualSongPercent)+10 //seek left 10%
		if(target >= 100){
			target = 99;
		}
		controller.seekTo(target);
	});
	$("#controllJumpRight").click(function(){
		controller.next();
	});
	$("#controllVolumeUp").click(function(){
		controller.volumeUp();
	});
	$("#controllVolumeDown").click(function(){
		controller.volumeDown();
	});
	$("#addYoutube").click(function(){
		controller.addYoutube();
	});
	
});

controller = {
    play: function(link){
    	var file = $(link).attr("target")
    	var folder = $(link).attr("folder")
       	var command = '/home/music/mpd-addandplay.sh "'+folder+'" "'+file+'"';
	    controller.sendCommand(command);  	
    },
    playRadio: function(link){
    	var file = $(link).attr("target")
       	var command = 'mpc clear && mpc add "'+file+'" && mpc play';
	    controller.sendCommand(command);  	
    },
    stop: function(){
    	controller.sendCommand("mpc stop"); 
    },
    next: function(){
    	controller.sendCommand("mpc next"); 
    },
    prev: function(){
    	controller.sendCommand("mpc prev"); 
    },
    togglePause: function(){
    	controller.sendCommand("mpc toggle"); 
    },
    seekTo: function(target){
    	var command = "mpc seek " + target + "%";
    	controller.sendCommand(command); 
    },
    volumeUp: function(){
    	controller.sendCommand("mpc volume +3"); 
    },
    volumeDown: function(){
    	controller.sendCommand("mpc volume -3"); 
    },
    sendCommand: function(command){
    	controller.showProgress();
    	var data = {command: command};
    	$.ajax( {
    		type : "POST",
    		data: data,
    		encoding:"UTF-8",
    		url : documentBaseUrl + "Service",
    		dataType : 'json',
    		success : function (data) {
    			controller.getCurrent()
    		},
    		error: function (data) {
    			console.log(data);
    		},
    		complete : function(e){
    			controller.hideProgress();
    		}
    	});
    },
    getCurrent: function(){
    	controller.showProgress();
    	var command = "mpc";
    	var data = {command: command};
    	$.ajax( {
    		type : "POST",
    		data: data,
    		encoding:"UTF-8",
    		url : documentBaseUrl + "Service",
    		dataType : 'json',
    		success : function (data) {
    			controller.processResponse(data.output)
    		},
    		error: function (data) {
    			controller.processResponse(data.output)
    		},
    		complete : function(e){
    			controller.hideProgress();
    		}
    	});
    },
    processResponse: function(response){
    	if(response.indexOf("Execution faiulre:") > -1){
	    	var h = response;
	    	$("#songController").css("display","none")
	    }else if(response.indexOf("[playing]") == -1 && response.indexOf("[stopped]") == -1 && response.indexOf("[paused]") == -1){
	    	var h = "nothing to play...";
	    	$("#songController").css("display","block");
	    }else{
			var s = response.split("\n")
			var h = s[0];
			if(s.length > 1){
			    h += "</br>"+s[1];
    			var myRe = /(\d\d?)([%]+)/g;
    			var percent = myRe.exec(s[1])[1];
    			controller.actualSongPercent = percent
    			if(s.length > 2){
    			    var a = s[2];
    			    var myRe = /[volume: ](\d\d?)([%]+)/g;
    			    var volume = myRe.exec(a)[1];
    			    h += " vol: "+volume+"%";
    			    controller.actualVolume  = volume;
    			}
    			
			}
			$("#songController").css("display","block")
		}
	    $("#currentSongTtitle").html(h);
    },
    getFiles: function(link){
    	if(link ==""){
    		var path = "";
    	}else{
    	    var path = $(link).attr("target");
    	}
    	    
    	controller.showProgress();
    	var command = 'mpc ls "' + path + '"';
    	var data = {command: command};
    	$.ajax( {
    		type : "POST",
    		encoding:"UTF-8",
    		data: data,
    		url : documentBaseUrl + "Service",
    		dataType : 'json',
    		success : function (data) {
    			if(data.result){
    				var list = controller.createList(data.output);
    			    controller.updateTable($("#localTable"), list, path, "file");
    			}
    		},
    		error: function (data) {
    		    //$("div.current").html(data.output)
    		    //controller.processResponse(data.output)
    		},
    		complete : function(e){
    			controller.hideProgress();
    		}
    	});
    },
    updateTable: function(table,list,path,type){
    	table.find('tbody').empty();
   		if(list.length == 0){
   			return;
   		}
   		if(path != ""){
   		    list = [{"file":"<<<","name":"<<<"}].concat(list);
   		}
   		    
    	for(var i=0; i<list.length; i++){
	        var file = list[i].file;
	        var name = list[i].name;

	        if(file == ""){
    			return; 
    		}else if(file == "<<<"){
    			var filePath = controller.getPathParent(path); 
    			
    		}else{
    			var filePath = file;
    		}
    		// file
    		var icon = "";
    		if(file.indexOf(".") > -1){
    			if(type=="file"){
    				var link = "controller.play(this)"
    			}else{
    			    var link = "controller.playRadio(this)"
    			}
    			    
    		    var icon = '<img src="images/music_icon.png" />'
    		}else{
    		// dir
    		    //var link = 'controller.getFiles("'+filePath+'")'
    		    var link = "controller.getFiles(this)"
    		    if(file != "<<<"){
    		        var icon = '<img src="images/folder_icon.png" />'
    		    }else{
    		    	var icon = '<img src="images/back.png" />'
    		    }
    		    
    		}
    		
    		table.find('tbody')
    	    .append($('<tr>')
    	        .append($('<td folder ="'+path+'" target="'+filePath+'" onclick="'+link+'" ontouchstart="return true">')
    	            .append($('<div>')
    	                .html(icon+name)
    	            )
    	        )
    	    );
        }
    },
    getStations: function(){
    	controller.updateTable($("#radioTable"),controller.radioChanels,"","stream")
    },
    getFileName: function(filename){
		var array = filename.split("/");
		if(array.length > 0){
			// remove extension
		    var filename = array.pop();
		    var d = filename.split(".mp3");
			return d[0];
		    
		}else{
			return filename;
		}
	},
	createList: function(data){
		if(typeof data == "undefined"){
		    return [];
		}
		
		var list = [];
		var array = data.split("\n");
		for(var i=0; i<array.length; i++){
			// skip .m3u files
		    if(array[i].indexOf(".m3u") > -1 ){
		    	continue;
		    }
		    
			var obj = {"file":array[i],"name":controller.getFileName(array[i])};
		    list.push(obj)
		}
		controller.fileList = list;
		return list;
		
	},
	getPathParent: function(path){
		if(path == ""){
		    return "";
		}
		var array = path.split("/");
		array.pop();
		return array.join("/");
	},
	showProgress: function(){
	    $("#progressIcon").css("display","block")
	},
	hideProgress: function(){
	    $("#progressIcon").css("display","none")
	},
	addYoutube: function(){
		$.confirm({
            'title'     : 'Download from youtube',
            'message'   : '<input type="text" id="youtubeLink" />',
            'buttons'   : {
                'OK'   : {
                    'class' : 'yes',
                    'action': function(){
                	    var youtubeLink = $("#youtubeLink").val();
                        if(youtubeLink != ""){
                            controller.saveYoutubeLink(youtubeLink);
                        }
                    }
                },
                'Cancel'    : {
                    'class' : 'no',
                    'action': function(){
                        // Nothing to do in this case. You can as well omit the action property.
                    }   
                }
            }
        });
	},
	saveYoutubeLink: function(youtubeLink){
   	    var command = "youtube-dl --extract-audio --audio-format mp3 -o '/home/pi/Music/youtube/%(title)s.%(ext)s' " + youtubeLink;
		//var command = "/home/pi/yt_download.sh " + youtubeLink;
        $("#currentSongTtitle").html("Start downloading...");
        controller.sendCommand(command); 
	},
	loadRadioChanels: function(documentBaseUrl) {
        var ur = documentBaseUrl;
        var uri = ur.substring(0, ur.lastIndexOf("/") + 1);
        $.ajax({
            type: "GET",
            dataType: 'html',
            async: false,
            timeout: 3000, // 3 sec
            url: documentBaseUrl + 'js/radioChanels.js?rand=' + Math.random(),
            success: function(data) {
            	controller.radioChanels = eval(data);
            },
            error: function() {
                // fallback english translation
                console.log("can't load radio channels")
            },
            complete: function(e) {
                // nothing to do
            }
        });
    }
}


