  connector Frame_a
    "Coordinate system fixed to the component with one cut-force and cut-torque (filled rectangular icon)"
    extends Frame;

    annotation (defaultComponentName="frame_a",
     Icon(coordinateSystem(
          preserveAspectRatio=true,
          extent={{-100,-100},{100,100}},
          grid={1,1},
          initialScale=0.16), graphics={Rectangle(
            extent={{-10,10},{10,-10}},
            lineColor={95,95,95},
            lineThickness=0.5), Rectangle(
            extent={{-30,100},{30,-100}},
            lineColor={0,0,0},
            fillColor={192,192,192},
            fillPattern=FillPattern.Solid)}),
     Diagram(coordinateSystem(
          preserveAspectRatio=true,
          extent={{-100,-100},{100,100}},
          grid={1,1},
          initialScale=0.16), graphics={Text(
            extent={{-140,-50},{140,-88}},
            lineColor={0,0,0},
            textString="%name"), Rectangle(
            extent={{-12,40},{12,-40}},
            lineColor={0,0,0},
            fillColor={192,192,192},
            fillPattern=FillPattern.Solid)}),
      Documentation(info="<html>
<p>
Basic definition of a coordinate system that is fixed to a mechanical
component. In the origin of the coordinate system the cut-force
and the cut-torque is acting.
This component has a filled rectangular icon.
</p>
</html>"));
  end Frame_a;
