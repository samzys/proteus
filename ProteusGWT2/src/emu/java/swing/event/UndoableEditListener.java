/*
 * @(#)UndoableEditListener.java	1.18 10/03/23
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package emu.java.swing.event;

/**
 * Interface implemented by a class interested in hearing about
 * undoable operations.
 *
 * @version 1.18 03/23/10
 * @author Ray Ryan
 */

public interface UndoableEditListener extends java.util.EventListener {

    /**
     * An undoable edit happened
     */
    void undoableEditHappened(UndoableEditEvent e);
}
