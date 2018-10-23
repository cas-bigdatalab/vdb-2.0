<?xml version="1.0"?>
<tools>
	<data type="string" key="dollar">$</data>
	<data type="string" key="D">$</data>
	<data type="string" key="hash">#</data>
	<data type="string" key="H">#</data>
	<data type="string" key="comment">##</data>
	<data type="string" key="C">##</data>

    <data type="number" key="TOOLS_VERSION" value="2.0"/>
    <data type="boolean" key="GENERIC_TOOLS_AVAILABLE" value="true"/>
    <toolbox scope="application">
        <tool class="org.apache.velocity.tools.generic.AlternatorTool"/>
        <tool class="org.apache.velocity.tools.generic.ClassTool"/>
        <tool class="org.apache.velocity.tools.generic.ComparisonDateTool"/>
        <tool class="org.apache.velocity.tools.generic.ConversionTool"/>
        <tool class="org.apache.velocity.tools.generic.DisplayTool"/>
        <tool class="org.apache.velocity.tools.generic.EscapeTool"/>
        <!--
        <tool class="org.apache.velocity.tools.generic.FieldTool"/>
        -->
        <tool class="org.apache.velocity.tools.generic.MathTool"/>
        <tool class="org.apache.velocity.tools.generic.NumberTool"/>
        <tool class="org.apache.velocity.tools.generic.ResourceTool"/>
        <tool class="org.apache.velocity.tools.generic.SortTool"/>
        <tool class="org.apache.velocity.tools.generic.XmlTool"/>
    </toolbox>
    <toolbox scope="request">
    	<!--
        <tool class="org.apache.velocity.tools.generic.ContextTool"/>
        -->
        <tool class="org.apache.velocity.tools.generic.LinkTool"/>
        <tool class="org.apache.velocity.tools.generic.LoopTool"/>
        <tool class="org.apache.velocity.tools.generic.RenderTool"/>
        <!-- 
        This is not useful in its default form.
        But, if it were, it'd be request-scoped.
        <tool class="org.apache.velocity.tools.generic.ValueParser"/>
        -->
    </toolbox>
    
</tools>