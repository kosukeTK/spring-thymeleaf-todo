package com.kosuke.utils;

import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PDFBox {

	public void createPDF() {
		try {
			// PDDocumentオブジェクトを生成して、Pageも設定しておく
			PDDocument document = new PDDocument();
			PDPage page = new PDPage();
			document.addPage(page);

			// コンテンツを設定するStreamオブジェクトを生成する
			PDPageContentStream contentStream = new PDPageContentStream(document, page);

			// フォントとポジションを指定してコンテンツを書き込む
			contentStream.beginText();
			contentStream.setFont(PDType1Font.HELVETICA, 20);
			contentStream.moveTextPositionByAmount(100, 700);
			contentStream.drawString("Hello World");
			contentStream.endText();

			// Streamオブジェクトを閉じる
			contentStream.close();

			// ファイルを保存して閉じる
			document.save("helloWorld.pdf");
			document.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (COSVisitorException e2) {
			e2.printStackTrace();
		}
	}
}
