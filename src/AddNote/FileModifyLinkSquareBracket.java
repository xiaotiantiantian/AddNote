/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AddNote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Zhirun Tian
 *
 *         This class is used to link notes with its signs using [] for CUHK
 *         Series books have notes but they are not linked I want to add links
 *         by using this class the original format likes <sup>[16-18]</sup> the
 *         original content of note likes
 *         <p class="annotation-text">
 *         [16-18]徐達深主編：《中華人民共和國實錄》第三卷（下）（長春：吉林人民出版社�?994），�?
 *         291�?293�?294-1295�?/p>
 *
 *         then I would change it to <sup><a id="footnote-16-18-backlink"
 *         href="part0019.xhtml#footnote-16-18">[16-18]</a></sup>
 *         <p>
 *         <a id="footnote-16-18"
 *         href="part0019.xhtml#footnote-16-18-backlink">[
 *         16-18]</a>徐達深主編：《中�
 *         �人民共和國實錄》第三卷（下）（長春：吉林人民出版社�?994），�
 *         ?291�?293�?294-1295�?/p>
 */
public class FileModifyLinkSquareBracket {

	// the symbol we used to mark the footnote
	private String[] SpecialNote;
	// the method to build the footnote
	// first is to build it at the end of each html file(to make it defult)
	// second is to insert a new html file at the folder(it would also change
	// the opf file to make the html shown on the end of the ebook)
	private int FootNoteProcessMode;

	// //then is the file path
	// private String FilePath;
	private File file;

	// the buffer would be used to store the notes
	private String tempBuffer;

	public FileModifyLinkSquareBracket(File file) {
		this.file = file;
	}

	/**
	 * 
	 *
	 * @param filePath
	 * @return
	 */
	public String read() {
		String filePath = this.file.getAbsolutePath();
		BufferedReader br = null;
		String line = null;
		StringBuffer buf = new StringBuffer();

		try {
			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {
				String regexSplit = "\\[|\\]";
				String[] tempLines = line.split(regexSplit);

				if (line.indexOf("class=\"annotation-text\"") < 0) {
					if (tempLines.length >= 2) {
						String NewLine = "";
						//if there is some place have useless "注"
						//replace it with blank string
						tempLines[1].replace("注", "");
						
						NewLine += tempLines[0] + "<a id=\"footnote-"
								+ tempLines[1] + "\" href=\"" + file.getName()
								+ "#footnote-" + tempLines[1] + "-backlink\">["
								+ tempLines[1] + "]</a>" + tempLines[2];
						buf.append(NewLine);
					}

					else {
						buf.append(line);
					}
				} else if (tempLines.length >= 2) {

					String NewLine = "";
					for (int i = 0; i < (tempLines.length - tempLines.length % 2); i = i + 2) {
						//if there is some place have useless "注"
						//replace it with blank string
						tempLines[i+1].replace("注", "");
						NewLine += tempLines[i];
						NewLine += "<a id=\"footnote-" + tempLines[i + 1]
								+ "-backlink\" href=\"" + file.getName()
								+ "#footnote-" + tempLines[i + 1] + "\">["
								+ tempLines[i + 1] + "]</a>";
					}
					if (tempLines.length % 2 != 0) {
						NewLine += tempLines[tempLines.length - 1];
					}
					buf.append(NewLine);
					System.out.println("----------------------");
				} else {
					buf.append(line);
				}

				buf.append(System.getProperty("line.separator"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					br = null;
				}
			}
		}
		// buf.append(bufNote.toString());

		return buf.toString();
	}

	/**
	 * 
	 *
	 * @param filePath
	 * @param content
	 */
	public void write(String content) {

		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
			bw.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					bw = null;
				}
			}
		}
	}

	public String[] getSpecialNote() {
		return SpecialNote;
	}

	public void setSpecialNote(String SpecialNote[]) {
		this.SpecialNote = SpecialNote;
	}

	public int getFootNoteProcessMode() {
		return FootNoteProcessMode;
	}

	public void setFootNoteProcessMode(int FootNoteProcessMode) {
		this.FootNoteProcessMode = FootNoteProcessMode;
	}

}
