/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.reporting.report.renderer;

import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.ReportRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

/**
 * ReportRenderer that renders to a default XML format
 */
@Handler
@Localized("reporting.JSONTemplateRenderer")
public class JSONReportRenderer extends ReportDesignRenderer {

	/**
	 * @see ReportRenderer#getFilename(ReportRequest)
	 */
    @Override
	public String getFilename(ReportRequest request) {
		return getFilenameBase(request) + ".json";
	}

	/**
	 * @see ReportRenderer#getRenderedContentType(ReportRequest)
	 */
	public String getRenderedContentType(ReportRequest request) {
		return "text/json";
	}

	/**
	 * @see ReportRenderer#render(ReportData, String, OutputStream)
	 */
//	Generates JSON file
	public void render(ReportData results, String argument, OutputStream out) throws IOException, RenderingException {

		Writer w = new OutputStreamWriter(out, "UTF-8");

		w.write("{\n");
		for (String dsKey : results.getDataSets().keySet()) {
			DataSet dataset = results.getDataSets().get(dsKey);
			List<DataSetColumn> columns = dataset.getMetaData().getColumns();
			w.write("\"dataValues\":[\n");
			for (DataSetRow row : dataset) {
				w.write("\t\t{");
				for (DataSetColumn column : columns) {
					Object colValue = row.getColumnValue(column);
					w.write("\"" + column.getLabel() + "\":");

						if (colValue != null) {
							w.write("\"");
							if (colValue instanceof Cohort) {

								w.write(Integer.toString(((Cohort) colValue).size()));
							} else {
								w.write(colValue.toString());
							}
							w.write("\"");
							w.write(",");
						}
						w.write("\n");
					}

				w.write("}");
				w.write(",");
			}
		}
		w.write("]\n");
		w.write("}\n");
		w.flush();
	}
}
