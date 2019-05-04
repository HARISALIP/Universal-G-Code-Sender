package com.willwinder.universalgcodesender.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PartialPositionTest {

    @Test
    public void testFormatted() {
        assertEquals("Y0Z0", new PartialPosition(null, 0.0, 0.0).getFormattedGCode());
        assertEquals("X0Z0", new PartialPosition(0.0, null, 0.0).getFormattedGCode());
        assertEquals("X0Y0", new PartialPosition(0.0, 0.0).getFormattedGCode());

        assertEquals("Y10Z0", new PartialPosition(null, 10.0, 0.0).getFormattedGCode());
        assertEquals("X10Z0", new PartialPosition(10.0, null, 0.0).getFormattedGCode());
        assertEquals("X0Y10", new PartialPosition(0.0, 10.0).getFormattedGCode());

        assertEquals("Y10Z-20", new PartialPosition(null, 10.0, -20.0).getFormattedGCode());
        assertEquals("X10Z-20", new PartialPosition(10.0, null, -20.0).getFormattedGCode());
        assertEquals("X-20Y10", new PartialPosition(-20.0, 10.0).getFormattedGCode());

        assertEquals("Y10.5Z-20.05", new PartialPosition(null, 10.5, -20.05).getFormattedGCode());
        assertEquals("X10.5Z-20.05", new PartialPosition(10.5, null, -20.05).getFormattedGCode());
        assertEquals("X-20.05Y10.5", new PartialPosition(-20.05, 10.5).getFormattedGCode());

        assertEquals("X5.2Y10.5Z-20.05", new PartialPosition(5.2, 10.5, -20.05).getFormattedGCode());
        assertEquals("X10.5Y5.2Z-20.05", new PartialPosition(10.5, 5.2, -20.05).getFormattedGCode());
        assertEquals("X-20.05Y10.5Z5.2", new PartialPosition(-20.05, 10.5, 5.2).getFormattedGCode());

        assertEquals("Y10.5Z-20.05", new PartialPosition.Builder().setY(10.5).setZ(-20.05).build().getFormattedGCode());
        assertEquals("X10.5Z-20.05", new PartialPosition.Builder().setX(10.5).setZ(-20.05).build().getFormattedGCode());
        assertEquals("X-20.05Y10.5", new PartialPosition.Builder().setY(10.5).setX(-20.05).build().getFormattedGCode());

        assertEquals("Y10.5Z-20.05", new PartialPosition.Builder()
                .setValue(Axis.Y, 10.5).setValue(Axis.Z, -20.05).build()
                .getFormattedGCode());
        assertEquals("X10.5Z-20.05", new PartialPosition.Builder()
                .setValue(Axis.X, 10.5).setValue(Axis.Z, -20.05).build()
                .getFormattedGCode());
        assertEquals("X-20.05Y10.5", new PartialPosition.Builder()
                .setValue(Axis.Y, 10.5).setValue(Axis.X, -20.05).build()
                .getFormattedGCode());

    }

    @Test
    public void testUnits() {
        PartialPosition p = new PartialPosition(1.0, 2.0, 3.0, UnitUtils.Units.MM);
        PartialPosition same = p.getPositionIn(UnitUtils.Units.MM);
        PartialPosition inch = p.getPositionIn(UnitUtils.Units.INCH);

        assertEquals(p.getX(), same.getX());
        assertEquals(p.getFormattedGCode(), same.getFormattedGCode());
        assertEquals(p, same);

        // use hardcoded factor instead of UnitUtils.scaleUnits - to find bugs if something goes wrong there
        assertEquals(p.getX(), inch.getX() * 25.4, 0.000001);
        assertNotEquals(p.getFormattedGCode(), inch.getFormattedGCode());
        assertNotEquals(p, inch);
    }

    @Test
    public void testPosition() {
        Position p = new Position(1, 2, 3, UnitUtils.Units.MM);
        Position pInch = new Position(1, 2, 3, UnitUtils.Units.INCH);

        PartialPosition pp = PartialPosition.from(p);
        PartialPosition ppInch = PartialPosition.from(pInch);

        assertEquals(pp.getX(), 1.0, 0.000001);
        assertEquals(pp.getY(), 2.0, 0.000001);
        assertEquals(pp.getZ(), 3.0, 0.000001);
        assertEquals(pp.getUnits(), UnitUtils.Units.MM);


        assertEquals(ppInch.getX(), 1.0, 0.000001);
        assertEquals(ppInch.getY(), 2.0, 0.000001);
        assertEquals(ppInch.getZ(), 3.0, 0.000001);
        assertEquals(ppInch.getUnits(), UnitUtils.Units.INCH);

        PartialPosition pTInch = PartialPosition.from(p.getPositionIn(UnitUtils.Units.INCH));

        assertEquals(pTInch, pp.getPositionIn(UnitUtils.Units.INCH));

        PartialPosition ppxy = PartialPosition.fromXY(p);
        assertEquals(ppxy.getX(), 1.0, 0.000001);
        assertEquals(ppxy.getY(), 2.0, 0.000001);
        assertFalse(ppxy.hasZ());
    }
}