var onResize = function() {
  // apply dynamic padding at the top of the body according to the fixed navbar height
  $("body").css("padding-top", $(".navbar-fixed-top").height());
  $("body").fadeTo('fast', 1, function() {
      // Animation complete.
  });
};

// attach the function to the window resize event
$(window).resize(onResize);

// call it also when the page is ready after load or reload
$(function() {
  onResize();
});
