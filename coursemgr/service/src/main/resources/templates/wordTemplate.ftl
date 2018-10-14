<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<?mso-application progid="Word.Document"?>
<pkg:package xmlns:pkg="http://schemas.microsoft.com/office/2006/xmlPackage">
	<pkg:part pkg:contentType="application/vnd.openxmlformats-package.relationships+xml" pkg:name="/_rels/.rels" pkg:padding="512">
		<pkg:xmlData>
			<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
				<Relationship Id="rId3" Target="docProps/app.xml" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties"/>
				<Relationship Id="rId2" Target="docProps/core.xml" Type="http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties"/>
				<Relationship Id="rId1" Target="word/document.xml" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument"/>
				<Relationship Id="rId4" Target="docProps/custom.xml" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/custom-properties"/>
			</Relationships>
		</pkg:xmlData>
	</pkg:part>
	<pkg:part pkg:contentType="application/vnd.openxmlformats-package.relationships+xml" pkg:name="/word/_rels/document.xml.rels" pkg:padding="256">
		<pkg:xmlData>
			<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
				<Relationship Id="rId3" Target="settings.xml" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings"/>
				<Relationship Id="rId2" Target="styles.xml" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles"/>
				<Relationship Id="rId1" Target="../customXml/item1.xml" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXml"/>
				<Relationship Id="rId6" Target="theme/theme1.xml" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme"/>
				<Relationship Id="rId5" Target="fontTable.xml" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/fontTable"/>
				<Relationship Id="rId4" Target="webSettings.xml" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/webSettings"/>
			</Relationships>
		</pkg:xmlData>
	</pkg:part>
	<pkg:part pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml" pkg:name="/word/document.xml">
		<pkg:xmlData>
			<w:document xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:v="urn:schemas-microsoft-com:vml" xmlns:ve="http://schemas.openxmlformats.org/markup-compatibility/2006" xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" xmlns:w10="urn:schemas-microsoft-com:office:word" xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml" xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing">
				<w:body>
					<#list taskList as task>
						<w:p w:rsidR="00721AAE" w:rsidRDefault="00327C36">
							<w:pPr>
								<w:pStyle w:val="1"/>
							</w:pPr>
							<w:r>
								<w:rPr>
									<w:rFonts w:hint="eastAsia"/>
								</w:rPr>
								<w:t>${task.taskName}</w:t>
							</w:r>
						</w:p>
						<w:p w:rsidR="00721AAE" w:rsidRDefault="00327C36">
							<w:pPr>
								<w:rPr>
									<w:sz w:val="24"/>
								</w:rPr>
							</w:pPr>
							<w:r>
								<w:rPr>
									<w:rFonts w:hint="eastAsia"/>
									<w:sz w:val="24"/>
								</w:rPr>
								<w:t>任务总分：</w:t>
							</w:r>
							<w:r>
								<w:rPr>
									<w:rFonts w:hint="eastAsia"/>
									<w:sz w:val="24"/>
								</w:rPr>
								<w:t xml:space="preserve">${task.taskTotalScore}   </w:t>
							</w:r>
							<w:r>
								<w:rPr>
									<w:rFonts w:hint="eastAsia"/>
									<w:sz w:val="24"/>
								</w:rPr>
								<w:t>学生总分：</w:t>
							</w:r>
							<w:r>
								<w:rPr>
									<w:rFonts w:hint="eastAsia"/>
									<w:sz w:val="24"/>
								</w:rPr>
								<w:t>${task.studentTotalScore}</w:t>
							</w:r>
						</w:p>
						<#list task.questionList as question>
							<w:p w:rsidR="00721AAE" w:rsidRDefault="00327C36">
								<w:pPr>
									<w:rPr>
										<w:rFonts w:ascii="黑体" w:cs="黑体" w:eastAsia="黑体" w:hAnsi="黑体"/>
										<w:sz w:val="28"/>
										<w:szCs w:val="28"/>
									</w:rPr>
								</w:pPr>
								<w:r>
									<w:rPr>
										<w:rFonts w:ascii="黑体" w:cs="黑体" w:eastAsia="黑体" w:hAnsi="黑体" w:hint="eastAsia"/>
										<w:sz w:val="28"/>
										<w:szCs w:val="28"/>
									</w:rPr>
									<w:t>${question.questionStems}</w:t>
								</w:r>
							</w:p>
							<#list question.optionList as option>
								<w:p w:rsidR="00721AAE" w:rsidRDefault="00327C36">
									<w:pPr>
										<w:rPr>
											<w:sz w:val="24"/>
										</w:rPr>
									</w:pPr>
									<w:r>
										<w:rPr>
											<w:rFonts w:hint="eastAsia"/>
											<w:sz w:val="24"/>
										</w:rPr>
										<w:t>${option.optionDes}</w:t>
									</w:r>
								</w:p>
							</#list>
							<w:p w:rsidR="00721AAE" w:rsidRDefault="00327C36">
								<w:pPr>
									<w:rPr>
										<w:color w:val="FF0000"/>
										<w:szCs w:val="21"/>
									</w:rPr>
								</w:pPr>
								<w:r>
									<w:rPr>
										<w:rFonts w:hint="eastAsia"/>
										<w:color w:val="FF0000"/>
										<w:szCs w:val="21"/>
									</w:rPr>
									<w:t>标准答案：</w:t>
								</w:r>
								<w:r>
									<w:rPr>
										<w:rFonts w:hint="eastAsia"/>
										<w:color w:val="FF0000"/>
										<w:szCs w:val="21"/>
									</w:rPr>
									<w:t>${question.standardAnswer}</w:t>
								</w:r>
							</w:p>
							<w:p w:rsidR="00721AAE" w:rsidRDefault="00327C36">
								<w:pPr>
									<w:rPr>
										<w:color w:val="FF0000"/>
										<w:szCs w:val="21"/>
									</w:rPr>
								</w:pPr>
								<w:r>
									<w:rPr>
										<w:rFonts w:hint="eastAsia"/>
										<w:color w:val="FF0000"/>
										<w:szCs w:val="21"/>
									</w:rPr>
									<w:t>学生答案：</w:t>
								</w:r>
								<w:r>
									<w:rPr>
										<w:rFonts w:hint="eastAsia"/>
										<w:color w:val="FF0000"/>
										<w:szCs w:val="21"/>
									</w:rPr>
									<w:t>${question.studentAnswer}</w:t>
								</w:r>
								<w:bookmarkStart w:id="0" w:name="_GoBack"/>
								<w:bookmarkEnd w:id="0"/>
							</w:p>
						</#list>
						<w:p w:rsidR="00721AAE" w:rsidRDefault="00721AAE">
							<w:pPr>
								<w:rPr>
									<w:sz w:val="24"/>
								</w:rPr>
							</w:pPr>
						</w:p>
						<w:p w:rsidR="00721AAE" w:rsidRDefault="00721AAE">
							<w:pPr>
								<w:rPr>
									<w:sz w:val="24"/>
								</w:rPr>
							</w:pPr>
						</w:p>
						<w:p w:rsidR="00721AAE" w:rsidRDefault="00721AAE">
							<w:pPr>
								<w:rPr>
									<w:sz w:val="24"/>
								</w:rPr>
							</w:pPr>
						</w:p>
					</#list>
					<w:sectPr w:rsidR="00721AAE" w:rsidSect="00721AAE">
						<w:pgSz w:h="16838" w:w="11906"/>
						<w:pgMar w:bottom="1440" w:footer="992" w:gutter="0" w:header="851" w:left="1800" w:right="1800" w:top="1440"/>
						<w:cols w:space="425"/>
						<w:docGrid w:linePitch="312" w:type="lines"/>
					</w:sectPr>
				</w:body>
			</w:document>
		</pkg:xmlData>
	</pkg:part>
	<pkg:part pkg:contentType="application/vnd.openxmlformats-officedocument.theme+xml" pkg:name="/word/theme/theme1.xml">
		<pkg:xmlData>
			<a:theme name="Office 主题" xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
				<a:themeElements>
					<a:clrScheme name="Office">
						<a:dk1>
							<a:sysClr lastClr="000000" val="windowText"/>
						</a:dk1>
						<a:lt1>
							<a:sysClr lastClr="FFFFFF" val="window"/>
						</a:lt1>
						<a:dk2>
							<a:srgbClr val="44546A"/>
						</a:dk2>
						<a:lt2>
							<a:srgbClr val="E7E6E6"/>
						</a:lt2>
						<a:accent1>
							<a:srgbClr val="5B9BD5"/>
						</a:accent1>
						<a:accent2>
							<a:srgbClr val="ED7D31"/>
						</a:accent2>
						<a:accent3>
							<a:srgbClr val="A5A5A5"/>
						</a:accent3>
						<a:accent4>
							<a:srgbClr val="FFC000"/>
						</a:accent4>
						<a:accent5>
							<a:srgbClr val="4472C4"/>
						</a:accent5>
						<a:accent6>
							<a:srgbClr val="70AD47"/>
						</a:accent6>
						<a:hlink>
							<a:srgbClr val="0563C1"/>
						</a:hlink>
						<a:folHlink>
							<a:srgbClr val="954F72"/>
						</a:folHlink>
					</a:clrScheme>
					<a:fontScheme name="Office">
						<a:majorFont>
							<a:latin typeface="Calibri Light"/>
							<a:ea typeface=""/>
							<a:cs typeface=""/>
							<a:font script="Jpan" typeface="ＭＳ ゴシック"/>
							<a:font script="Hang" typeface="맑은 고딕"/>
							<a:font script="Hans" typeface="宋体"/>
							<a:font script="Hant" typeface="新細明體"/>
							<a:font script="Arab" typeface="Times New Roman"/>
							<a:font script="Hebr" typeface="Times New Roman"/>
							<a:font script="Thai" typeface="Angsana New"/>
							<a:font script="Ethi" typeface="Nyala"/>
							<a:font script="Beng" typeface="Vrinda"/>
							<a:font script="Gujr" typeface="Shruti"/>
							<a:font script="Khmr" typeface="MoolBoran"/>
							<a:font script="Knda" typeface="Tunga"/>
							<a:font script="Guru" typeface="Raavi"/>
							<a:font script="Cans" typeface="Euphemia"/>
							<a:font script="Cher" typeface="Plantagenet Cherokee"/>
							<a:font script="Yiii" typeface="Microsoft Yi Baiti"/>
							<a:font script="Tibt" typeface="Microsoft Himalaya"/>
							<a:font script="Thaa" typeface="MV Boli"/>
							<a:font script="Deva" typeface="Mangal"/>
							<a:font script="Telu" typeface="Gautami"/>
							<a:font script="Taml" typeface="Latha"/>
							<a:font script="Syrc" typeface="Estrangelo Edessa"/>
							<a:font script="Orya" typeface="Kalinga"/>
							<a:font script="Mlym" typeface="Kartika"/>
							<a:font script="Laoo" typeface="DokChampa"/>
							<a:font script="Sinh" typeface="Iskoola Pota"/>
							<a:font script="Mong" typeface="Mongolian Baiti"/>
							<a:font script="Viet" typeface="Times New Roman"/>
							<a:font script="Uigh" typeface="Microsoft Uighur"/>
							<a:font script="Geor" typeface="Sylfaen"/>
						</a:majorFont>
						<a:minorFont>
							<a:latin typeface="Calibri"/>
							<a:ea typeface=""/>
							<a:cs typeface=""/>
							<a:font script="Jpan" typeface="ＭＳ 明朝"/>
							<a:font script="Hang" typeface="맑은 고딕"/>
							<a:font script="Hans" typeface="宋体"/>
							<a:font script="Hant" typeface="新細明體"/>
							<a:font script="Arab" typeface="Arial"/>
							<a:font script="Hebr" typeface="Arial"/>
							<a:font script="Thai" typeface="Cordia New"/>
							<a:font script="Ethi" typeface="Nyala"/>
							<a:font script="Beng" typeface="Vrinda"/>
							<a:font script="Gujr" typeface="Shruti"/>
							<a:font script="Khmr" typeface="DaunPenh"/>
							<a:font script="Knda" typeface="Tunga"/>
							<a:font script="Guru" typeface="Raavi"/>
							<a:font script="Cans" typeface="Euphemia"/>
							<a:font script="Cher" typeface="Plantagenet Cherokee"/>
							<a:font script="Yiii" typeface="Microsoft Yi Baiti"/>
							<a:font script="Tibt" typeface="Microsoft Himalaya"/>
							<a:font script="Thaa" typeface="MV Boli"/>
							<a:font script="Deva" typeface="Mangal"/>
							<a:font script="Telu" typeface="Gautami"/>
							<a:font script="Taml" typeface="Latha"/>
							<a:font script="Syrc" typeface="Estrangelo Edessa"/>
							<a:font script="Orya" typeface="Kalinga"/>
							<a:font script="Mlym" typeface="Kartika"/>
							<a:font script="Laoo" typeface="DokChampa"/>
							<a:font script="Sinh" typeface="Iskoola Pota"/>
							<a:font script="Mong" typeface="Mongolian Baiti"/>
							<a:font script="Viet" typeface="Arial"/>
							<a:font script="Uigh" typeface="Microsoft Uighur"/>
							<a:font script="Geor" typeface="Sylfaen"/>
						</a:minorFont>
					</a:fontScheme>
					<a:fmtScheme name="Office">
						<a:fillStyleLst>
							<a:solidFill>
								<a:schemeClr val="phClr"/>
							</a:solidFill>
							<a:gradFill rotWithShape="1">
								<a:gsLst>
									<a:gs pos="0">
										<a:schemeClr val="phClr">
											<a:lumMod val="110000"/>
											<a:satMod val="105000"/>
											<a:tint val="67000"/>
										</a:schemeClr>
									</a:gs>
									<a:gs pos="50000">
										<a:schemeClr val="phClr">
											<a:lumMod val="105000"/>
											<a:satMod val="103000"/>
											<a:tint val="73000"/>
										</a:schemeClr>
									</a:gs>
									<a:gs pos="100000">
										<a:schemeClr val="phClr">
											<a:lumMod val="105000"/>
											<a:satMod val="109000"/>
											<a:tint val="81000"/>
										</a:schemeClr>
									</a:gs>
								</a:gsLst>
								<a:lin ang="5400000" scaled="0"/>
							</a:gradFill>
							<a:gradFill rotWithShape="1">
								<a:gsLst>
									<a:gs pos="0">
										<a:schemeClr val="phClr">
											<a:satMod val="103000"/>
											<a:lumMod val="102000"/>
											<a:tint val="94000"/>
										</a:schemeClr>
									</a:gs>
									<a:gs pos="50000">
										<a:schemeClr val="phClr">
											<a:satMod val="110000"/>
											<a:lumMod val="100000"/>
											<a:shade val="100000"/>
										</a:schemeClr>
									</a:gs>
									<a:gs pos="100000">
										<a:schemeClr val="phClr">
											<a:lumMod val="99000"/>
											<a:satMod val="120000"/>
											<a:shade val="78000"/>
										</a:schemeClr>
									</a:gs>
								</a:gsLst>
								<a:lin ang="5400000" scaled="0"/>
							</a:gradFill>
						</a:fillStyleLst>
						<a:lnStyleLst>
							<a:ln algn="ctr" cap="flat" cmpd="sng" w="6350">
								<a:solidFill>
									<a:schemeClr val="phClr"/>
								</a:solidFill>
								<a:prstDash val="solid"/>
								<a:miter lim="800000"/>
							</a:ln>
							<a:ln algn="ctr" cap="flat" cmpd="sng" w="12700">
								<a:solidFill>
									<a:schemeClr val="phClr"/>
								</a:solidFill>
								<a:prstDash val="solid"/>
								<a:miter lim="800000"/>
							</a:ln>
							<a:ln algn="ctr" cap="flat" cmpd="sng" w="19050">
								<a:solidFill>
									<a:schemeClr val="phClr"/>
								</a:solidFill>
								<a:prstDash val="solid"/>
								<a:miter lim="800000"/>
							</a:ln>
						</a:lnStyleLst>
						<a:effectStyleLst>
							<a:effectStyle>
								<a:effectLst/>
							</a:effectStyle>
							<a:effectStyle>
								<a:effectLst/>
							</a:effectStyle>
							<a:effectStyle>
								<a:effectLst>
									<a:outerShdw algn="ctr" blurRad="57150" dir="5400000" dist="19050" rotWithShape="0">
										<a:srgbClr val="000000">
											<a:alpha val="63000"/>
										</a:srgbClr>
									</a:outerShdw>
								</a:effectLst>
							</a:effectStyle>
						</a:effectStyleLst>
						<a:bgFillStyleLst>
							<a:solidFill>
								<a:schemeClr val="phClr"/>
							</a:solidFill>
							<a:solidFill>
								<a:schemeClr val="phClr">
									<a:tint val="95000"/>
									<a:satMod val="170000"/>
								</a:schemeClr>
							</a:solidFill>
							<a:gradFill rotWithShape="1">
								<a:gsLst>
									<a:gs pos="0">
										<a:schemeClr val="phClr">
											<a:tint val="93000"/>
											<a:satMod val="150000"/>
											<a:shade val="98000"/>
											<a:lumMod val="102000"/>
										</a:schemeClr>
									</a:gs>
									<a:gs pos="50000">
										<a:schemeClr val="phClr">
											<a:tint val="98000"/>
											<a:satMod val="130000"/>
											<a:shade val="90000"/>
											<a:lumMod val="103000"/>
										</a:schemeClr>
									</a:gs>
									<a:gs pos="100000">
										<a:schemeClr val="phClr">
											<a:shade val="63000"/>
											<a:satMod val="120000"/>
										</a:schemeClr>
									</a:gs>
								</a:gsLst>
								<a:lin ang="5400000" scaled="0"/>
							</a:gradFill>
						</a:bgFillStyleLst>
					</a:fmtScheme>
				</a:themeElements>
				<a:objectDefaults/>
				<a:extraClrSchemeLst/>
			</a:theme>
		</pkg:xmlData>
	</pkg:part>
	<pkg:part pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.settings+xml" pkg:name="/word/settings.xml">
		<pkg:xmlData>
			<w:settings xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:sl="http://schemas.openxmlformats.org/schemaLibrary/2006/main" xmlns:v="urn:schemas-microsoft-com:vml" xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" xmlns:w10="urn:schemas-microsoft-com:office:word">
				<w:zoom w:percent="90"/>
				<w:embedSystemFonts/>
				<w:bordersDoNotSurroundHeader/>
				<w:bordersDoNotSurroundFooter/>
				<w:defaultTabStop w:val="420"/>
				<w:drawingGridVerticalSpacing w:val="156"/>
				<w:displayHorizontalDrawingGridEvery w:val="0"/>
				<w:displayVerticalDrawingGridEvery w:val="2"/>
				<w:characterSpacingControl w:val="compressPunctuation"/>
				<w:compat>
					<w:spaceForUL/>
					<w:balanceSingleByteDoubleByteWidth/>
					<w:doNotLeaveBackslashAlone/>
					<w:ulTrailSpace/>
					<w:doNotExpandShiftReturn/>
					<w:adjustLineHeightInTable/>
					<w:useFELayout/>
				</w:compat>
				<w:rsids>
					<w:rsidRoot w:val="00721AAE"/>
					<w:rsid w:val="00327C36"/>
					<w:rsid w:val="00721AAE"/>
					<w:rsid w:val="040762A3"/>
					<w:rsid w:val="221F1F65"/>
					<w:rsid w:val="5717573B"/>
					<w:rsid w:val="57EC5099"/>
					<w:rsid w:val="6A135AFE"/>
				</w:rsids>
				<m:mathPr>
					<m:mathFont m:val="Cambria Math"/>
					<m:brkBin m:val="before"/>
					<m:brkBinSub m:val="--"/>
					<m:smallFrac m:val="off"/>
					<m:dispDef/>
					<m:lMargin m:val="0"/>
					<m:rMargin m:val="0"/>
					<m:defJc m:val="centerGroup"/>
					<m:wrapIndent m:val="1440"/>
					<m:intLim m:val="subSup"/>
					<m:naryLim m:val="undOvr"/>
				</m:mathPr>
				<w:themeFontLang w:eastAsia="zh-CN" w:val="en-US"/>
				<w:clrSchemeMapping w:accent1="accent1" w:accent2="accent2" w:accent3="accent3" w:accent4="accent4" w:accent5="accent5" w:accent6="accent6" w:bg1="light1" w:bg2="light2" w:followedHyperlink="followedHyperlink" w:hyperlink="hyperlink" w:t1="dark1" w:t2="dark2"/>
				<w:doNotIncludeSubdocsInStats/>
				<w:shapeDefaults>
					<o:shapedefaults spidmax="2050" v:ext="edit"/>
					<o:shapelayout v:ext="edit">
						<o:idmap data="1" v:ext="edit"/>
					</o:shapelayout>
				</w:shapeDefaults>
				<w:decimalSymbol w:val="."/>
				<w:listSeparator w:val=","/>
			</w:settings>
		</pkg:xmlData>
	</pkg:part>
	<pkg:part pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.styles+xml" pkg:name="/word/styles.xml">
		<pkg:xmlData>
			<w:styles xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">
				<w:docDefaults>
					<w:rPrDefault>
						<w:rPr>
							<w:rFonts w:asciiTheme="minorHAnsi" w:cstheme="minorBidi" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorHAnsi"/>
							<w:lang w:bidi="ar-SA" w:eastAsia="zh-CN" w:val="en-US"/>
						</w:rPr>
					</w:rPrDefault>
					<w:pPrDefault/>
				</w:docDefaults>
				<w:latentStyles w:count="267" w:defLockedState="0" w:defQFormat="0" w:defSemiHidden="0" w:defUIPriority="0" w:defUnhideWhenUsed="0">
					<w:lsdException w:name="Normal" w:qFormat="1"/>
					<w:lsdException w:name="heading 1" w:qFormat="1"/>
					<w:lsdException w:name="heading 2" w:qFormat="1" w:semiHidden="1" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="heading 3" w:qFormat="1" w:semiHidden="1" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="heading 4" w:qFormat="1" w:semiHidden="1" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="heading 5" w:qFormat="1" w:semiHidden="1" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="heading 6" w:qFormat="1" w:semiHidden="1" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="heading 7" w:qFormat="1" w:semiHidden="1" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="heading 8" w:qFormat="1" w:semiHidden="1" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="heading 9" w:qFormat="1" w:semiHidden="1" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="caption" w:qFormat="1" w:semiHidden="1" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="Title" w:qFormat="1"/>
					<w:lsdException w:name="Default Paragraph Font" w:semiHidden="1"/>
					<w:lsdException w:name="Subtitle" w:qFormat="1"/>
					<w:lsdException w:name="Strong" w:qFormat="1"/>
					<w:lsdException w:name="Emphasis" w:qFormat="1"/>
					<w:lsdException w:name="HTML Top of Form" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="HTML Bottom of Form" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="Normal Table" w:semiHidden="1"/>
					<w:lsdException w:name="No List" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="Outline List 1" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="Outline List 2" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="Outline List 3" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="Placeholder Text" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="No Spacing" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="Light Shading" w:uiPriority="60"/>
					<w:lsdException w:name="Light List" w:uiPriority="61"/>
					<w:lsdException w:name="Light Grid" w:uiPriority="62"/>
					<w:lsdException w:name="Medium Shading 1" w:uiPriority="63"/>
					<w:lsdException w:name="Medium Shading 2" w:uiPriority="64"/>
					<w:lsdException w:name="Medium List 1" w:uiPriority="65"/>
					<w:lsdException w:name="Medium List 2" w:uiPriority="66"/>
					<w:lsdException w:name="Medium Grid 1" w:uiPriority="67"/>
					<w:lsdException w:name="Medium Grid 2" w:uiPriority="68"/>
					<w:lsdException w:name="Medium Grid 3" w:uiPriority="69"/>
					<w:lsdException w:name="Dark List" w:uiPriority="70"/>
					<w:lsdException w:name="Colorful Shading" w:uiPriority="71"/>
					<w:lsdException w:name="Colorful List" w:uiPriority="72"/>
					<w:lsdException w:name="Colorful Grid" w:uiPriority="73"/>
					<w:lsdException w:name="Light Shading Accent 1" w:uiPriority="60"/>
					<w:lsdException w:name="Light List Accent 1" w:uiPriority="61"/>
					<w:lsdException w:name="Light Grid Accent 1" w:uiPriority="62"/>
					<w:lsdException w:name="Medium Shading 1 Accent 1" w:uiPriority="63"/>
					<w:lsdException w:name="Medium Shading 2 Accent 1" w:uiPriority="64"/>
					<w:lsdException w:name="Medium List 1 Accent 1" w:uiPriority="65"/>
					<w:lsdException w:name="Revision" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="List Paragraph" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="Quote" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="Intense Quote" w:semiHidden="1" w:uiPriority="99" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="Medium List 2 Accent 1" w:uiPriority="66"/>
					<w:lsdException w:name="Medium Grid 1 Accent 1" w:uiPriority="67"/>
					<w:lsdException w:name="Medium Grid 2 Accent 1" w:uiPriority="68"/>
					<w:lsdException w:name="Medium Grid 3 Accent 1" w:uiPriority="69"/>
					<w:lsdException w:name="Dark List Accent 1" w:uiPriority="70"/>
					<w:lsdException w:name="Colorful Shading Accent 1" w:uiPriority="71"/>
					<w:lsdException w:name="Colorful List Accent 1" w:uiPriority="72"/>
					<w:lsdException w:name="Colorful Grid Accent 1" w:uiPriority="73"/>
					<w:lsdException w:name="Light Shading Accent 2" w:uiPriority="60"/>
					<w:lsdException w:name="Light List Accent 2" w:uiPriority="61"/>
					<w:lsdException w:name="Light Grid Accent 2" w:uiPriority="62"/>
					<w:lsdException w:name="Medium Shading 1 Accent 2" w:uiPriority="63"/>
					<w:lsdException w:name="Medium Shading 2 Accent 2" w:uiPriority="64"/>
					<w:lsdException w:name="Medium List 1 Accent 2" w:uiPriority="65"/>
					<w:lsdException w:name="Medium List 2 Accent 2" w:uiPriority="66"/>
					<w:lsdException w:name="Medium Grid 1 Accent 2" w:uiPriority="67"/>
					<w:lsdException w:name="Medium Grid 2 Accent 2" w:uiPriority="68"/>
					<w:lsdException w:name="Medium Grid 3 Accent 2" w:uiPriority="69"/>
					<w:lsdException w:name="Dark List Accent 2" w:uiPriority="70"/>
					<w:lsdException w:name="Colorful Shading Accent 2" w:uiPriority="71"/>
					<w:lsdException w:name="Colorful List Accent 2" w:uiPriority="72"/>
					<w:lsdException w:name="Colorful Grid Accent 2" w:uiPriority="73"/>
					<w:lsdException w:name="Light Shading Accent 3" w:uiPriority="60"/>
					<w:lsdException w:name="Light List Accent 3" w:uiPriority="61"/>
					<w:lsdException w:name="Light Grid Accent 3" w:uiPriority="62"/>
					<w:lsdException w:name="Medium Shading 1 Accent 3" w:uiPriority="63"/>
					<w:lsdException w:name="Medium Shading 2 Accent 3" w:uiPriority="64"/>
					<w:lsdException w:name="Medium List 1 Accent 3" w:uiPriority="65"/>
					<w:lsdException w:name="Medium List 2 Accent 3" w:uiPriority="66"/>
					<w:lsdException w:name="Medium Grid 1 Accent 3" w:uiPriority="67"/>
					<w:lsdException w:name="Medium Grid 2 Accent 3" w:uiPriority="68"/>
					<w:lsdException w:name="Medium Grid 3 Accent 3" w:uiPriority="69"/>
					<w:lsdException w:name="Dark List Accent 3" w:uiPriority="70"/>
					<w:lsdException w:name="Colorful Shading Accent 3" w:uiPriority="71"/>
					<w:lsdException w:name="Colorful List Accent 3" w:uiPriority="72"/>
					<w:lsdException w:name="Colorful Grid Accent 3" w:uiPriority="73"/>
					<w:lsdException w:name="Light Shading Accent 4" w:uiPriority="60"/>
					<w:lsdException w:name="Light List Accent 4" w:uiPriority="61"/>
					<w:lsdException w:name="Light Grid Accent 4" w:uiPriority="62"/>
					<w:lsdException w:name="Medium Shading 1 Accent 4" w:uiPriority="63"/>
					<w:lsdException w:name="Medium Shading 2 Accent 4" w:uiPriority="64"/>
					<w:lsdException w:name="Medium List 1 Accent 4" w:uiPriority="65"/>
					<w:lsdException w:name="Medium List 2 Accent 4" w:uiPriority="66"/>
					<w:lsdException w:name="Medium Grid 1 Accent 4" w:uiPriority="67"/>
					<w:lsdException w:name="Medium Grid 2 Accent 4" w:uiPriority="68"/>
					<w:lsdException w:name="Medium Grid 3 Accent 4" w:uiPriority="69"/>
					<w:lsdException w:name="Dark List Accent 4" w:uiPriority="70"/>
					<w:lsdException w:name="Colorful Shading Accent 4" w:uiPriority="71"/>
					<w:lsdException w:name="Colorful List Accent 4" w:uiPriority="72"/>
					<w:lsdException w:name="Colorful Grid Accent 4" w:uiPriority="73"/>
					<w:lsdException w:name="Light Shading Accent 5" w:uiPriority="60"/>
					<w:lsdException w:name="Light List Accent 5" w:uiPriority="61"/>
					<w:lsdException w:name="Light Grid Accent 5" w:uiPriority="62"/>
					<w:lsdException w:name="Medium Shading 1 Accent 5" w:uiPriority="63"/>
					<w:lsdException w:name="Medium Shading 2 Accent 5" w:uiPriority="64"/>
					<w:lsdException w:name="Medium List 1 Accent 5" w:uiPriority="65"/>
					<w:lsdException w:name="Medium List 2 Accent 5" w:uiPriority="66"/>
					<w:lsdException w:name="Medium Grid 1 Accent 5" w:uiPriority="67"/>
					<w:lsdException w:name="Medium Grid 2 Accent 5" w:uiPriority="68"/>
					<w:lsdException w:name="Medium Grid 3 Accent 5" w:uiPriority="69"/>
					<w:lsdException w:name="Dark List Accent 5" w:uiPriority="70"/>
					<w:lsdException w:name="Colorful Shading Accent 5" w:uiPriority="71"/>
					<w:lsdException w:name="Colorful List Accent 5" w:uiPriority="72"/>
					<w:lsdException w:name="Colorful Grid Accent 5" w:uiPriority="73"/>
					<w:lsdException w:name="Light Shading Accent 6" w:uiPriority="60"/>
					<w:lsdException w:name="Light List Accent 6" w:uiPriority="61"/>
					<w:lsdException w:name="Light Grid Accent 6" w:uiPriority="62"/>
					<w:lsdException w:name="Medium Shading 1 Accent 6" w:uiPriority="63"/>
					<w:lsdException w:name="Medium Shading 2 Accent 6" w:uiPriority="64"/>
					<w:lsdException w:name="Medium List 1 Accent 6" w:uiPriority="65"/>
					<w:lsdException w:name="Medium List 2 Accent 6" w:uiPriority="66"/>
					<w:lsdException w:name="Medium Grid 1 Accent 6" w:uiPriority="67"/>
					<w:lsdException w:name="Medium Grid 2 Accent 6" w:uiPriority="68"/>
					<w:lsdException w:name="Medium Grid 3 Accent 6" w:uiPriority="69"/>
					<w:lsdException w:name="Dark List Accent 6" w:uiPriority="70"/>
					<w:lsdException w:name="Colorful Shading Accent 6" w:uiPriority="71"/>
					<w:lsdException w:name="Colorful List Accent 6" w:uiPriority="72"/>
					<w:lsdException w:name="Colorful Grid Accent 6" w:uiPriority="73"/>
					<w:lsdException w:name="Subtle Emphasis" w:qFormat="1" w:uiPriority="19"/>
					<w:lsdException w:name="Intense Emphasis" w:qFormat="1" w:uiPriority="21"/>
					<w:lsdException w:name="Subtle Reference" w:qFormat="1" w:uiPriority="31"/>
					<w:lsdException w:name="Intense Reference" w:qFormat="1" w:uiPriority="32"/>
					<w:lsdException w:name="Book Title" w:qFormat="1" w:uiPriority="33"/>
					<w:lsdException w:name="Bibliography" w:semiHidden="1" w:uiPriority="37" w:unhideWhenUsed="1"/>
					<w:lsdException w:name="TOC Heading" w:qFormat="1" w:semiHidden="1" w:uiPriority="39" w:unhideWhenUsed="1"/>
				</w:latentStyles>
				<w:style w:default="1" w:styleId="a" w:type="paragraph">
					<w:name w:val="Normal"/>
					<w:qFormat/>
					<w:rsid w:val="00721AAE"/>
					<w:pPr>
						<w:widowControl w:val="0"/>
						<w:jc w:val="both"/>
					</w:pPr>
					<w:rPr>
						<w:kern w:val="2"/>
						<w:sz w:val="21"/>
						<w:szCs w:val="24"/>
					</w:rPr>
				</w:style>
				<w:style w:styleId="1" w:type="paragraph">
					<w:name w:val="heading 1"/>
					<w:basedOn w:val="a"/>
					<w:next w:val="a"/>
					<w:qFormat/>
					<w:rsid w:val="00721AAE"/>
					<w:pPr>
						<w:keepNext/>
						<w:keepLines/>
						<w:spacing w:line="576" w:lineRule="auto"/>
						<w:jc w:val="center"/>
						<w:outlineLvl w:val="0"/>
					</w:pPr>
					<w:rPr>
						<w:b/>
						<w:kern w:val="44"/>
						<w:sz w:val="44"/>
					</w:rPr>
				</w:style>
				<w:style w:default="1" w:styleId="a0" w:type="character">
					<w:name w:val="Default Paragraph Font"/>
					<w:uiPriority w:val="1"/>
					<w:semiHidden/>
					<w:unhideWhenUsed/>
				</w:style>
				<w:style w:default="1" w:styleId="a1" w:type="table">
					<w:name w:val="Normal Table"/>
					<w:uiPriority w:val="99"/>
					<w:semiHidden/>
					<w:unhideWhenUsed/>
					<w:qFormat/>
					<w:tblPr>
						<w:tblInd w:type="dxa" w:w="0"/>
						<w:tblCellMar>
							<w:top w:type="dxa" w:w="0"/>
							<w:left w:type="dxa" w:w="108"/>
							<w:bottom w:type="dxa" w:w="0"/>
							<w:right w:type="dxa" w:w="108"/>
						</w:tblCellMar>
					</w:tblPr>
				</w:style>
				<w:style w:default="1" w:styleId="a2" w:type="numbering">
					<w:name w:val="No List"/>
					<w:uiPriority w:val="99"/>
					<w:semiHidden/>
					<w:unhideWhenUsed/>
				</w:style>
				<w:style w:styleId="a3" w:type="paragraph">
					<w:name w:val="Document Map"/>
					<w:basedOn w:val="a"/>
					<w:link w:val="Char"/>
					<w:rsid w:val="00327C36"/>
					<w:rPr>
						<w:rFonts w:ascii="宋体" w:eastAsia="宋体"/>
						<w:sz w:val="18"/>
						<w:szCs w:val="18"/>
					</w:rPr>
				</w:style>
				<w:style w:customStyle="1" w:styleId="Char" w:type="character">
					<w:name w:val="文档结构图 Char"/>
					<w:basedOn w:val="a0"/>
					<w:link w:val="a3"/>
					<w:rsid w:val="00327C36"/>
					<w:rPr>
						<w:rFonts w:ascii="宋体" w:eastAsia="宋体"/>
						<w:kern w:val="2"/>
						<w:sz w:val="18"/>
						<w:szCs w:val="18"/>
					</w:rPr>
				</w:style>
			</w:styles>
		</pkg:xmlData>
	</pkg:part>
	<pkg:part pkg:contentType="application/vnd.openxmlformats-officedocument.customXmlProperties+xml" pkg:name="/customXml/itemProps1.xml" pkg:padding="32">
		<pkg:xmlData pkg:originalXmlStandalone="no">
			<ds:datastoreItem ds:itemID="{B1977F7D-205B-4081-913C-38D41E755F92}" xmlns:ds="http://schemas.openxmlformats.org/officeDocument/2006/customXml">
				<ds:schemaRefs>
					<ds:schemaRef ds:uri="http://www.wps.cn/officeDocument/2013/wpsCustomData"/>
				</ds:schemaRefs>
			</ds:datastoreItem>
		</pkg:xmlData>
	</pkg:part>
	<pkg:part pkg:contentType="application/vnd.openxmlformats-package.relationships+xml" pkg:name="/customXml/_rels/item1.xml.rels" pkg:padding="256">
		<pkg:xmlData>
			<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
				<Relationship Id="rId1" Target="itemProps1.xml" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXmlProps"/>
			</Relationships>
		</pkg:xmlData>
	</pkg:part>
	<pkg:part pkg:contentType="application/vnd.openxmlformats-officedocument.custom-properties+xml" pkg:name="/docProps/custom.xml" pkg:padding="256">
		<pkg:xmlData>
			<Properties xmlns="http://schemas.openxmlformats.org/officeDocument/2006/custom-properties" xmlns:vt="http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes">
				<property fmtid="{D5CDD505-2E9C-101B-9397-08002B2CF9AE}" name="KSOProductBuildVer" pid="2">
					<vt:lpwstr>2052-10.1.0.7520</vt:lpwstr>
				</property>
			</Properties>
		</pkg:xmlData>
	</pkg:part>
	<pkg:part pkg:contentType="application/vnd.openxmlformats-officedocument.extended-properties+xml" pkg:name="/docProps/app.xml" pkg:padding="256">
		<pkg:xmlData>
			<Properties xmlns="http://schemas.openxmlformats.org/officeDocument/2006/extended-properties" xmlns:vt="http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes">
				<Template>Normal.dotm</Template>
				<TotalTime>0</TotalTime>
				<Pages>1</Pages>
				<Words>20</Words>
				<Characters>116</Characters>
				<Application>Microsoft Office Word</Application>
				<DocSecurity>0</DocSecurity>
				<Lines>1</Lines>
				<Paragraphs>1</Paragraphs>
				<ScaleCrop>false</ScaleCrop>
				<Company/>
				<LinksUpToDate>false</LinksUpToDate>
				<CharactersWithSpaces>135</CharactersWithSpaces>
				<SharedDoc>false</SharedDoc>
				<HyperlinksChanged>false</HyperlinksChanged>
				<AppVersion>12.0000</AppVersion>
			</Properties>
		</pkg:xmlData>
	</pkg:part>
	<pkg:part pkg:contentType="application/vnd.openxmlformats-package.core-properties+xml" pkg:name="/docProps/core.xml" pkg:padding="256">
		<pkg:xmlData>
			<cp:coreProperties xmlns:cp="http://schemas.openxmlformats.org/package/2006/metadata/core-properties" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dcmitype="http://purl.org/dc/dcmitype/" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
				<dc:creator>Administrator</dc:creator>
				<cp:lastModifiedBy>Administrator</cp:lastModifiedBy>
				<cp:revision>2</cp:revision>
				<dcterms:created xsi:type="dcterms:W3CDTF">2018-10-14T02:41:00Z</dcterms:created>
				<dcterms:modified xsi:type="dcterms:W3CDTF">2018-10-14T02:41:00Z</dcterms:modified>
			</cp:coreProperties>
		</pkg:xmlData>
	</pkg:part>
	<pkg:part pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.fontTable+xml" pkg:name="/word/fontTable.xml">
		<pkg:xmlData>
			<w:fonts xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">
				<w:font w:name="Calibri">
					<w:panose1 w:val="020F0502020204030204"/>
					<w:charset w:val="00"/>
					<w:family w:val="swiss"/>
					<w:pitch w:val="variable"/>
					<w:sig w:csb0="0000019F" w:csb1="00000000" w:usb0="E00002FF" w:usb1="4000ACFF" w:usb2="00000001" w:usb3="00000000"/>
				</w:font>
				<w:font w:name="宋体">
					<w:altName w:val="SimSun"/>
					<w:panose1 w:val="02010600030101010101"/>
					<w:charset w:val="86"/>
					<w:family w:val="auto"/>
					<w:pitch w:val="variable"/>
					<w:sig w:csb0="00040001" w:csb1="00000000" w:usb0="00000003" w:usb1="288F0000" w:usb2="00000016" w:usb3="00000000"/>
				</w:font>
				<w:font w:name="Times New Roman">
					<w:panose1 w:val="02020603050405020304"/>
					<w:charset w:val="00"/>
					<w:family w:val="roman"/>
					<w:pitch w:val="variable"/>
					<w:sig w:csb0="000001FF" w:csb1="00000000" w:usb0="E0002AFF" w:usb1="C0007841" w:usb2="00000009" w:usb3="00000000"/>
				</w:font>
				<w:font w:name="黑体">
					<w:altName w:val="SimHei"/>
					<w:panose1 w:val="02010609060101010101"/>
					<w:charset w:val="86"/>
					<w:family w:val="modern"/>
					<w:pitch w:val="fixed"/>
					<w:sig w:csb0="00040001" w:csb1="00000000" w:usb0="800002BF" w:usb1="38CF7CFA" w:usb2="00000016" w:usb3="00000000"/>
				</w:font>
				<w:font w:name="Calibri Light">
					<w:panose1 w:val="020F0302020204030204"/>
					<w:charset w:val="00"/>
					<w:family w:val="swiss"/>
					<w:pitch w:val="variable"/>
					<w:sig w:csb0="0000019F" w:csb1="00000000" w:usb0="A00002EF" w:usb1="4000207B" w:usb2="00000000" w:usb3="00000000"/>
				</w:font>
			</w:fonts>
		</pkg:xmlData>
	</pkg:part>
	<pkg:part pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.webSettings+xml" pkg:name="/word/webSettings.xml">
		<pkg:xmlData>
			<w:webSettings xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"/>
		</pkg:xmlData>
	</pkg:part>
	<pkg:part pkg:contentType="application/xml" pkg:name="/customXml/item1.xml" pkg:padding="32">
		<pkg:xmlData>
			<s:customData xmlns="http://www.wps.cn/officeDocument/2013/wpsCustomData" xmlns:s="http://www.wps.cn/officeDocument/2013/wpsCustomData">
				<customSectProps>
					<customSectPr/>
				</customSectProps>
			</s:customData>
		</pkg:xmlData>
	</pkg:part>
</pkg:package>