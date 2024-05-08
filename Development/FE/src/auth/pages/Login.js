import React, { useState, useContext } from "react";

import Input from "../../shared/components/FormElements/Input";
import Button from "../../shared/components/FormElements/Button";
// import ErrorModal from "../../shared/components/UIElements/ErrorModal";
// import LoadingSpinner from "../../shared/components/UIElements/LoadingSpinner";

import { VALIDATOR_REQUIRE, VALIDATOR_SAME_VALUE } from "../../shared/util/validators";
import { useForm } from "../../shared/hooks/form-hook";
// import { useHttpClient } from "../../shared/hooks/http-hook";
import { HttpContext } from "../../shared/context/http-context";
import { AuthContext } from "../../shared/context/auth-context";
import "./Login.css";

const Login = () => {
  const auth = useContext(AuthContext);
  const http = useContext(HttpContext);
  const [isFirst, setIsFirst] = useState(false);
  // const { isLoading, error, sendRequest, clearError } = useHttpClient();

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

    if (!isFirst) {
      try {
        const responseData = await http.sendRequest(
          `${process.env.REACT_APP_BACKEND_URL}/api/login`,
          "POST",
          JSON.stringify({
            uniName: formState.inputs.uniname.value,
            userName: formState.inputs.username.value,
            userPass: formState.inputs.password.value,
          }),
          {
            "Content-Type": "application/json",
          }
        );

        // // TODO : change Dummy DATA
        // const responseData = {
        //   is_first_login: true,
        //   userId: 1,
        //   token: "asdf",
        //   isAdmin: false,
        // };

        if (responseData.is_first_login) {
          setIsFirst(responseData.is_first_login);
        } else {
          auth.login(
            responseData.userId,
            responseData.token,
            responseData.isAdmin
          );
        }
      } catch (err) {}
    } else {
      try {
        // const formData = new FormData();
        // formData.append("userId", auth.userId);
        // formData.append("passInit", formState.inputs['password-initial'].value);
        // formData.append("passChange", formState.inputs['password-change'].value);
        // const responseData = await sendRequest(
        //   `${process.env.REACT_APP_BACKEND_URL}/api/changePW`,
        //   "PATCH",
        //   formData
        // );

        // TODO : change Dummy DATA
        const responseData = {
          userId: 1,
          token: "asdf",
          isAdmin: false,
        };
        // 비밀번호 변경 백에서 에러날 경우 에러 코드 수행 안되는지 확인하기

        //비밀번호 변경 후 로그인
        auth.login(
          responseData.userId,
          responseData.token,
          responseData.isAdmin
        );
      } catch (err) {}
    }
  };

  const formElement = !isFirst ? (
    <React.Fragment>
      <Input
        element="input"
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
      />
    </React.Fragment>
  ) : (
    <React.Fragment>
      <Input
        element="input"
        id="password-initial"
        type="password"
        placeholder="초기 비밀번호"
        validators={[VALIDATOR_REQUIRE()]}
        onInput={inputHandler}
        initialValue=""
      />
      <Input
        element="input"
        id="password-change"
        type="password"
        placeholder="변경할 비밀번호"
        validators={[VALIDATOR_REQUIRE()]}
        onInput={inputHandler}
        initialValue=""
      />
      <Input
        element="input"
        id="password-check"
        type="password"
        placeholder="비밀번호 확인"
        validators={[VALIDATOR_REQUIRE(), VALIDATOR_SAME_VALUE("password-change")]}
        onInput={inputHandler}
        initialValue=""
      />
    </React.Fragment>
  );

  return (
    <React.Fragment>
      {/* <ErrorModal error={error} onClear={clearError} /> */}
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
            {/* {http.isLoading && <LoadingSpinner asOverlay />} */}
            {isFirst && (
              <p className="form__firstlogin-text">
                최초 로그인 시 비밀번호를 변경해주세요.
              </p>
            )}
            <form onSubmit={authSubmitHandler}>
              {formElement}
              <Button type="submit" disabled={!formState.isValid}>
                {!isFirst ? "로그인" : "비밀번호 변경"}
              </Button>
            </form>
          </div>
        </div>
      </div>
    </React.Fragment>
  );
};

export default Login;
