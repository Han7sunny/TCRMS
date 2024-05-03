import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
// import Paper from '@material-ui/core/Paper';

import "./RegistIndividualTable.css";

const useStyles = makeStyles({
  table: {
    minWidth: 650,
  },
});

function createData(name, sex, foreigner, nationality,idnumber, event, weight) {
  return { name, sex, foreigner, nationality, idnumber, event, weight };
}

const rows = [
  createData('홍길동', '남자', true, '영국', '200101-2000000', ['겨루기'], '핀'),
  createData('', '', '', '', '', [], ''),
];

export default function RegistIndividualTable() {
  const classes = useStyles();
  const columns = ['','성명','성별','외국인','국적','주민등록번호','종목','체급(겨루기만)',''];

// https://v4.mui.com/components/tables/

  return (
    <TableContainer>
      <Table className={classes.table} aria-label="simple table">
        <TableHead>
          <TableRow>
            {/* <TableCell>Dessert (100g serving)</TableCell>
            <TableCell align="right">Calories</TableCell>
            <TableCell align="right">Fat&nbsp;(g)</TableCell>
            <TableCell align="right">Carbs&nbsp;(g)</TableCell>
            <TableCell align="right">Protein&nbsp;(g)</TableCell> */}
            {columns.map((col) => (<TableCell align="center">{col}</TableCell>))}
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((row, i) => (
            <TableRow key={row.name}>
              <TableCell scope="row">
                {i + 1}
              </TableCell>
              <TableCell scope="row">
                {row.name}
              </TableCell>
              <TableCell align="right">{row.sex}</TableCell>
              <TableCell align="right">{row.foreigner}</TableCell>
              <TableCell align="right">{row.nationality}</TableCell>
              <TableCell align="right">{row.idnumber}</TableCell>
              <TableCell align="right">{row.event}</TableCell>
              <TableCell align="right">{row.weight}</TableCell>
              <TableCell align="right"></TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}