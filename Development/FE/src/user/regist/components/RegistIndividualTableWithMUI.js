import React from "react";
import { withStyles, makeStyles } from "@material-ui/core/styles";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableContainer from "@material-ui/core/TableContainer";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";
import {
  TextField,
  Radio,
  RadioGroup,
  FormControlLabel,
} from "@material-ui/core";

import "./RegistIndividualTable.css";

const useStyles = makeStyles({
  table: {
    minWidth: 650,
  },
});

const StyledTableCell = withStyles((theme) => ({
  head: {
    backgroundColor: "white",
    color: "black",
  },
  body: {
    fontSize: 12,
  },
  root: {
    borderRight: "1px solid rgba(224, 224, 224, 1)",
    padding: "10px",
  },
}))(TableCell);

const StyledTableRow = withStyles((theme) => ({
  root: {
    "&:nth-of-type(odd)": {
      backgroundColor: theme.palette.action.hover,
    },
  },
}))(TableRow);

const StyledTextField = withStyles(() => ({
  root: {
    width: "100%",
    "& label": {
      fontSize: "12px",
      transform: "translate(10px, 10px) scale(1)",
    },
    "& input": {
      padding: "8px",
      fontSize: "12px",
    },
  },
}))(TextField);

function createData(
  name,
  sex,
  foreigner,
  nationality,
  idnumber,
  event,
  weight
) {
  return { name, sex, foreigner, nationality, idnumber, event, weight };
}

const rows = [
  createData(
    "홍길동",
    "남자",
    true,
    "영국",
    "200101-2000000",
    ["겨루기"],
    "핀"
  ),
  createData("", "", "", "", "", [], ""),
];

export default function RegistIndividualTable() {
  const classes = useStyles();
  const columns = [
    "",
    "성명",
    "성별",
    "외국인",
    "국적",
    "주민등록번호",
    "종목",
    "체급(겨루기만)",
    "",
  ];

  // https://v4.mui.com/components/tables/

  return (
    <TableContainer component={Paper}>
      <form>
        <Table className={classes.table} aria-label="simple table">
          <TableHead>
            <TableRow>
              {columns.map((col, i) => (
                <StyledTableCell align="center" key={i}>
                  {col}
                </StyledTableCell>
              ))}
            </TableRow>
          </TableHead>
          <TableBody>
            {rows.map((row, i) => (
              <StyledTableRow key={row.name}>
                <StyledTableCell scope="row">{i + 1}</StyledTableCell>
                <StyledTableCell scope="row">{row.name}</StyledTableCell>
                <StyledTableCell align="right">{row.sex}</StyledTableCell>
                <StyledTableCell align="right">{row.sex}</StyledTableCell>
                <StyledTableCell align="right">
                  {row.nationality}
                </StyledTableCell>
                <StyledTableCell align="right">{row.idnumber}</StyledTableCell>
                <StyledTableCell align="right">{row.event}</StyledTableCell>
                <StyledTableCell align="right">{row.weight}</StyledTableCell>
                <StyledTableCell align="right"></StyledTableCell>
              </StyledTableRow>
            ))}
            <StyledTableRow key={"3"}>
              <StyledTableCell scope="row">3</StyledTableCell>
              <StyledTableCell scope="row">
                <StyledTextField
                  id="outlined-password-input"
                  classes="outlined-name"
                  label="성명"
                  type="input"
                  variant="outlined"
                />
              </StyledTableCell>
              <StyledTableCell>
                <RadioGroup
                  aria-label="sex"
                  name="sex"
                  // value={value}
                  value="male"
                  // onChange={handleRadioChange}
                >
                  <FormControlLabel
                    value="male"
                    control={<Radio />}
                    label="남자"
                  />
                  <FormControlLabel
                    value="female"
                    control={<Radio />}
                    label="여자"
                  />
                </RadioGroup>
              </StyledTableCell>
              <StyledTableCell></StyledTableCell>
              <StyledTableCell></StyledTableCell>
              <StyledTableCell></StyledTableCell>
              <StyledTableCell></StyledTableCell>
              <StyledTableCell></StyledTableCell>
              <StyledTableCell></StyledTableCell>
            </StyledTableRow>
          </TableBody>
        </Table>
      </form>
    </TableContainer>
  );
}
