import React, { useCallback, useContext, useEffect, useState } from "react";
import { NavLink } from "react-router-dom";

import { AuthContext } from "../../context/auth-context";
import { HttpContext } from "../../context/http-context";
import Button from "../FormElements/Button";
import "./NavLinks.css";

const NavLinks = () => {
  const auth = useContext(AuthContext);
  const http = useContext(HttpContext);

  const calculateRemainingTime = useCallback(() => {
    const remainingTime =
      auth.tokenExpirationDate.getTime() - new Date().getTime();
    const remainingHours = Math.floor(remainingTime / (1000 * 60 * 60));
    const remainingMinutes = Math.floor(
      (remainingTime % (1000 * 60 * 60)) / (1000 * 60)
    );

    return remainingHours + "시간 " + remainingMinutes + "분";
  }, [auth.tokenExpirationDate]);

  // const [remainingTime, setRemainingTime] = useState(calculateRemainingTime());

  // useEffect(() => {
  //   const timer = setInterval(() => {
  //     setRemainingTime(calculateRemainingTime());
  //   }, 5000);

    // return () => {
    //   clearInterval(timer);
    // };
  // }, [auth.tokenExpirationDate, calculateRemainingTime]);

  const logout = async () => {
    try {
      // const responseData = await http.sendRequest(
      await http.sendRequest(
        `${process.env.REACT_APP_BACKEND_URL}/api/logout`,
        "POST",
        JSON.stringify({
          userId: auth.userId,
        }),
        {
          Authorization: `Bearer ${auth.token}`,
          "Content-Type": "application/json",
        }
      );
      auth.logout();
    } catch (err) {
      auth.logout();
    }
  };

  const userMenu = (
    <React.Fragment>
      <li>
        <NavLink to="/regist">신청</NavLink>
      </li>
      <ul>
        <li>
          <NavLink to="/regist/individual">개인전 신청</NavLink>
        </li>
        <li>
          <NavLink to="/regist/team">단체전 신청</NavLink>
        </li>
        <li>
          <NavLink to="/regist/second">세컨 신청</NavLink>
        </li>
        <li>
          <NavLink to="/regist/volunteer">자원봉사자 신청</NavLink>
        </li>
      </ul>
      <li>
        <NavLink to="/docu">서류제출</NavLink>
      </li>
      <li>
        <NavLink to="/submit">신청내역</NavLink>
      </li>
    </React.Fragment>
  );

  const adminMenu = (
    <React.Fragment>
      <li>
        <NavLink to="">서류 일괄보기</NavLink>
      </li>
      <li>
        <NavLink to="">등록내역(1차)</NavLink>
      </li>
      <li>
        <NavLink to="">최종내역(2차)</NavLink>
      </li>
    </React.Fragment>
  );

  return (
    <React.Fragment>
      <div className="nav-links__logo">
        <img
          src={`${process.env.PUBLIC_URL}/img/KUTCA_logo.png`}
          alt=""
          width="64%"
        />
      </div>
      <ul className="nav-links">{auth.isAdmin ? adminMenu : userMenu}</ul>
      <div className="nav-links__logout" onClick={logout}>
        {/* <div className="text-remain-time">남은 시간 : {remainingTime}</div> */}
        <Button type="submit">로그아웃</Button>
      </div>
    </React.Fragment>
  );
};

export default NavLinks;
