import React, { useContext, useRef } from "react";

import Input from "../../shared/components/FormElements/Input";
import Button from "../../shared/components/FormElements/Button";

import {
  VALIDATOR_REQUIRE,
  VALIDATOR_SAME_VALUE,
} from "../../shared/util/validators";
import { useForm } from "../../shared/hooks/form-hook";
import { HttpContext } from "../../shared/context/http-context";
import { AuthContext } from "../../shared/context/auth-context";
import "./Login.css";

const Login = () => {
  const auth = useContext(AuthContext);
  const http = useContext(HttpContext);

  const inputPassInitRef = useRef(null);
  const inputUniname = useRef(null);

  const [formState, inputHandler, setFormData] = useForm(
    {
      uniname: {
        value: "",
        isValid: false,
      },
      username: {
        value: "",
        isValid: false,
      },
      password: {
        value: "",
        isValid: false,
      },
    },
    false
  );

  const authSubmitHandler = async (event) => {
    event.preventDefault();

    if (!auth.isFirstLogin) {
      try {
        const responseData = await http.sendRequest(
          `${process.env.REACT_APP_BACKEND_URL}/api/login`,
          "POST",
          JSON.stringify({
            universityName: formState.inputs.uniname.value,
            username: formState.inputs.username.value,
            password: formState.inputs.password.value,
          }),
          {
            "Content-Type": "application/json",
          },
          "로그인 실패"
        );

        // // TODO : change Dummy DATA
        // const responseData = {
        //   is_first_login: true,
        //   userId: 1,
        //   token: "asdf",
        //   isAdmin: false,
        // };

        const responsePayload = responseData.payload;

        const isAdmin = responsePayload.auth === "ADMIN" ? true : false;
        if (responseData.isFirstLogin) {
          inputUniname.current.focus();
        }
        auth.login(
          responsePayload.userId,
          responsePayload.token,
          isAdmin,
          responsePayload.isFirstLogin
        );

        if (responsePayload.isFirstLogin) {
          inputPassInitRef.current.focus();
          setFormData(
            {
              "password-initial": {
                value: "",
                isValid: false,
              },
              "password-change": {
                value: "",
                isValid: false,
              },
              "password-check": {
                value: "",
                isValid: false,
              },
            },
            false
          );
        }
      } catch (err) {}
    } else {
      try {
        // if (formState.inputs["password-initial"].value !== "password") {
        // inputPassInitRef.current.focus();
        inputPassInitRef.current.clear();
        //   throw new Error(
        //     "초기 비밀번호가 일치하지 않습니다. 다시 입력해주세요."
        //   );
        // }

        const responseData = await http.sendRequest(
          `${process.env.REACT_APP_BACKEND_URL}/api/changePW`,
          "POST",
          JSON.stringify({
            userId: auth.userId,
            initPassword: formState.inputs["password-initial"].value,
            newPassword: formState.inputs["password-change"].value,
          }),
          {
            Authorization: `Bearer ${auth.token}`,
            "Content-Type": "application/json",
          },
          "비밀번호 변경 실패"
        );

        if (responseData.isSuccess) {
          alert("비밀번호가 변경되었습니다.");
        }
        // // TODO : change Dummy DATA
        // const responseData = {
        //   userId: 1,
        //   token: "asdf",
        //   isAdmin: false,
        // };
        // 비밀번호 변경 백에서 에러날 경우 에러 코드 수행 안되는지 확인하기

        //비밀번호 변경 후 로그인
        auth.login(auth.userId, auth.token, auth.isAdmin, false);
      } catch (err) {
        // http.setError(err.message);
      }
    }
  };

  const formElement = !auth.isFirstLogin ? (
    <React.Fragment>
      <Input
        element="input"
        ref={inputUniname}
        id="uniname"
        type="text"
        placeholder="학교명"
        validators={[VALIDATOR_REQUIRE()]}
        onInput={inputHandler}
      />
      <Input
        element="input"
        id="username"
        type="text"
        placeholder="대표자명"
        validators={[VALIDATOR_REQUIRE()]}
        onInput={inputHandler}
      />
      <Input
        element="input"
        id="password"
        type="password"
        placeholder="비밀번호"
        validators={[VALIDATOR_REQUIRE()]}
        onInput={inputHandler}
        autoComplete="off"
      />
    </React.Fragment>
  ) : (
    <React.Fragment>
      <Input
        element="input"
        ref={inputPassInitRef}
        id="password-initial"
        type="password"
        placeholder="초기 비밀번호"
        validators={[VALIDATOR_REQUIRE()]}
        onInput={inputHandler}
        initialValue=""
        autoComplete="off"
      />
      <Input
        element="input"
        id="password-change"
        type="password"
        placeholder="변경할 비밀번호"
        validators={[VALIDATOR_REQUIRE()]}
        onInput={inputHandler}
        initialValue=""
        autoComplete="off"
      />
      <Input
        element="input"
        id="password-check"
        type="password"
        placeholder="비밀번호 확인"
        validators={[
          VALIDATOR_REQUIRE(),
          VALIDATOR_SAME_VALUE("password-change"),
        ]}
        onInput={inputHandler}
        initialValue=""
        errorText="비밀번호를 확인해주세요."
        autoComplete="off"
      />
    </React.Fragment>
  );

  return (
    <React.Fragment>
      <div className="authentication">
        <div>
          <div className="kutca-logo">
            <img
              src={`${process.env.PUBLIC_URL}/img/KUTCA_logo.png`}
              alt=""
              width="200px"
            />
          </div>
          <div className="authentication-form">
            {auth.isFirstLogin && (
              <p className="form__firstlogin-text">
                최초 로그인 시 비밀번호를 변경해주세요.
              </p>
            )}
            <form onSubmit={authSubmitHandler}>
              {formElement}
              <Button type="submit" disabled={!formState.isValid}>
                {!auth.isFirstLogin ? "로그인" : "비밀번호 변경"}
              </Button>
            </form>
          </div>
        </div>
      </div>
    </React.Fragment>
  );
};

export default Login;
