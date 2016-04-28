(function($){
	
	$.confirm = function(params){
		
		if($('#confirmOverlay').length){
			// A confirm is already shown on the page:
			return false;
		}
		
		var buttonHTML = '';
		$.each(params.buttons,function(name,obj){
			
			// Generating the markup for the buttons:
			// LS use buttonLabel for name if exists
			if(obj['buttonLabel'] != undefined && obj['buttonLabel'] != ""){
			    var label = obj['buttonLabel'];
			}else{
				var label = name;
			}
			
			
			buttonHTML += '<button href="#" class="button '+obj['class']+'">'+label+'</button>';
			
			if(!obj.action){
				obj.action = function(){};
			}
		});
		
		var markup = [
			'<div id="confirmOverlay">',
			'<div id="confirmBox">',
			'<h1>',params.title,'</h1>',
			'<p>',params.message,'</p>',
			'<div id="confirmButtons">',
			buttonHTML,
			'</div></div></div>'
		].join('');
		
		
		$(markup).hide().appendTo('body').show();
		// LS set width from obj params
		if(params.width){
			$('#confirmBox').css("width", params.width);
		}
		
		$("#confirmButtons a.yes").focus();
		
		var buttons = $('#confirmBox .button'),
			i = 0;

		$.each(params.buttons,function(name,obj){
			buttons.eq(i++).click(function(){
				
				// Calling the action attribute when a
				// click occurs, and hiding the confirm.
				
				obj.action();
				$.confirm.hide();
				return false;
			});
		});
	}

	$.confirm.hide = function(){
		//$('#confirmOverlay').fadeOut(function(){
			$('#confirmOverlay').remove();
		//});
	}
	
})(jQuery);