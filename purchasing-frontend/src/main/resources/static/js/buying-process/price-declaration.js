(function($) {
	
$( "#select-evaluation-type .select button" ).on( "click", function( event ) {
	evaluationTypeList.onclick_setButtons(this);
});

$(function() {
	evaluationTypeList.setSelected($('#select-evaluation-type #evaluation-type-item-' + $('#' + evaluationTypeList.FIELD_EVALUATION_TYPE).val() + ' .select button'));
	continueButton.set();
});

var evaluationTypeList = {
	CLASS_SELECTED : "selected",
	FIELD_EVALUATION_TYPE : "evaluationType",
	onclick_setButtons : function(el) {
		$('#' + this.FIELD_EVALUATION_TYPE)
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
		$("#select-evaluation-type .select button")
			.removeClass(this.CLASS_SELECTED)
			.html('Select');
	}
		
}

var continueButton = {
	_button : $("#continue"),
	set : function() {
		if ($("#" + evaluationTypeList.FIELD_EVALUATION_TYPE).val().length == 0) {
			this._button.prop("disabled", true);
		} else {
			this._button.prop("disabled", false);
		}
	}
}
	
}(jQuery));