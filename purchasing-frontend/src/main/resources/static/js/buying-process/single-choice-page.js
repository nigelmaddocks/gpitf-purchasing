(function($) {
	
$( "#select-options .select button" ).on( "click", function( event ) {
	optionsList.onclick_setButtons(this);
});

$(function() {
	optionsList.setSelected($('#select-options #option-item-' + $('#' + optionsList.FIELD_SELECTED_OPTION).val() + ' .select button'));
	continueButton.set();
});

var optionsList = {
	CLASS_SELECTED : "selected",
	FIELD_SELECTED_OPTION : "selectedOption",
	onclick_setButtons : function(el) {
		$('#' + this.FIELD_SELECTED_OPTION)
			.val( $(el).val() );
		this.setUnselected();
		this.setSelected(el);
		continueButton.set();
	},
	setSelected : function(el) {
		$(el)
			.addClass(this.CLASS_SELECTED)
			.html('<span class="glyphicon glyphicon-ok"/>Selected');
	},
	setUnselected : function() {
		$("#select-options .select button")
			.removeClass(this.CLASS_SELECTED)
			.html('Select');
	}
		
}

var continueButton = {
	_button : $("#continue"),
	set : function() {
		if ($("#" + optionsList.FIELD_SELECTED_OPTION).val().length == 0) {
			this._button.prop("disabled", true);
		} else {
			this._button.prop("disabled", false);
		}
	}
}
	
}(jQuery));