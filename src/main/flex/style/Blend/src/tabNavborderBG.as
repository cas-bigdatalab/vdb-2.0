package 
{
	
	import flash.display.*;
	import flash.geom.*;
	import flash.utils.*;	
	import mx.core.EdgeMetrics;
	import mx.skins.halo.HaloBorder;
	import mx.utils.GraphicsUtil;
	
	public class tabNavborderBG extends HaloBorder 
	{
		
		private var topCornerRad:Number;		
		private var bottomCornerRad:Number;	
		private var fillColors:Array;
		private var borderFillColors:Array;	
		private var setup:Boolean;
		
		
		private function setupStyles():void
		{
			fillColors = getStyle("fillColors") as Array;
			if (!fillColors) fillColors = [0x000000, 0x34465b, 0x000000];
			
			borderFillColors = getStyle("borderFillColors") as Array;
			if (!borderFillColors) borderFillColors = [0x8F0F0F, 0x000000];
			
			topCornerRad = getStyle("cornerRadius") as Number;
			if (!topCornerRad) topCornerRad = 0;	
			
			bottomCornerRad = getStyle("bottomCornerRad") as Number;
			if (!bottomCornerRad) bottomCornerRad = topCornerRad;	
		
		}
		
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			super.updateDisplayList(unscaledWidth, unscaledHeight);	
			
			setupStyles();
			
			var g:Graphics = graphics;
			var e:EdgeMetrics = borderMetrics;
			var w:Number = unscaledWidth - e.left - e.right;
			var h:Number = unscaledHeight - e.top - e.bottom;
			var m:Matrix = horizontalGradientMatrix(0, 0, w, h);
			var n:Matrix = verticalGradientMatrix(0, 0, w, h);
		
			g.beginGradientFill("linear", fillColors, [1, 1, 1], [0, 127, 255], m);
			
			g.lineStyle(3,0xCCCCCC,.9);
			g.lineGradientStyle("linear", borderFillColors, [1,.3], [0, 255], n);
									
			GraphicsUtil.drawRoundRectComplex(g, e.left + 1, e.top, w - 3, h - 2, 0, 16, 16, 16);
			g.endFill(); 
										
		}
		
	}
}