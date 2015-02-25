package com.craigburke.document.core.factory

import com.craigburke.document.core.Paragraph
import com.craigburke.document.core.Text
import com.craigburke.document.core.Row
import com.craigburke.document.core.Cell

/**
 * Factory for cell nodes
 * @author Craig Burke
 */
class CellFactory extends AbstractFactory {

	boolean isLeaf() { false }
    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, node, Map attributes) { false }

	def newInstance(FactoryBuilderSupport builder, name, value, Map attributes) {
		Cell cell = new Cell(attributes)
		Row row = builder.current

		cell.font = cell.font ?: builder.font.clone()
		cell.position = builder.tablePosition.cell
        if (builder.addCellToRow) {
            builder.addCellToRow(cell, row)
        }

		Paragraph paragraph = new Paragraph(font:cell.font.clone(), parent:cell)
		paragraph.margin.setDefaults(0, 0)
        if (builder.addParagraphToCell) {
            builder.addParagraphToCell(paragraph, cell)
        }
		cell.children << paragraph

		if (value) {
			Text text = new Text(value:value, font:cell.font.clone(), parent:paragraph)
		    paragraph.children << text
            if (builder.addTextToParagraph) {
                builder.addTextToParagraph(text, paragraph)
            }
		}

		cell
	}

	void onNodeCompleted(FactoryBuilderSupport builder, row, cell) {
		if (builder.onCellComplete) {
			builder.onCellComplete(cell, row)
		}
   	}

}
