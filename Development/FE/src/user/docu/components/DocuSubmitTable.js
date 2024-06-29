import React, { useState } from "react";

import "./DocuSubmitTable.css";
import { deepEqual } from "../../../shared/util/deepEqual";
import DocuSubmitTableHeader from "./DocuSubmitTableHeader";
import DocuSubmitTableRow from "./DocuSubmitTableRow";

function areEqual(prevProps, nextProps) {
  console.log(prevProps);
  console.log(nextProps);
  // 같으면 true 다르면 false
  return (
    // prevProps.isEditable === nextProps.isEditable &&
    // prevProps.hideText === nextProps.hideText &&
    deepEqual(prevProps.data, nextProps.data)
  );
}

const DocuSubmitTable = React.memo((props) => {
  const [hideText, setHideText] = useState(true);

  let flatColumns = [];
  props.columns.forEach((col) => {
    if (col.columns) {
      flatColumns.push(...col.columns);
    } else flatColumns.push(col);
  });

  return (
    // <form action="#" method="POST" encType="multipart/form-data">
    <table className="docu-table">
      <DocuSubmitTableHeader
        flatColumns={flatColumns}
        showNumber={props.showNumber}
        columns={props.columns}
        setHideText={setHideText}
        hideText={hideText}
      />
      <tbody>
        {props.data.map((row, i) => (
          <DocuSubmitTableRow
            key={i}
            showNumber={props.showNumber}
            flatColumns={flatColumns}
            rowData={row}
            rowIdx={i}
            type={props.type}
            hideText={hideText}
            onFileSubmit={props.onFileSubmit}
            onFilesSubmit={props.onFilesSubmit}
          />
          // <tr key={i}>
          //   {props.showNumber && <td>{i + 1}</td>}
          //   {flatColumns.map((col, j) => (
          //     <td key={"row" + i + "col" + j}>
          //       {inputField(col, row[col.id], i, j)}
          //     </td>
          //   ))}
          // </tr>
        ))}
      </tbody>
    </table>
    // </form>
  );
}, areEqual);

export default DocuSubmitTable;
