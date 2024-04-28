import React, { useState, useContext } from "react";

import Input from "../../shared/components/FormElements/Input";
import Button from "../../shared/components/FormElements/Button";
import ErrorModal from "../../shared/components/UIElements/ErrorModal";
import LoadingSpinner from "../../shared/components/UIElements/LoadingSpinner";

import { VALIDATOR_REQUIRE } from "../../shared/util/validators";
import { useForm } from "../../shared/hooks/form-hook";
import { useHttpClient } from "../../shared/hooks/http-hook";
import { AuthContext } from "../../shared/context/auth-context";
import "./Login.css";

const Login = () => {
  const auth = useContext(AuthContext);
  const [isFirst, setIsFirst] = useState(false);
  const { isLoading, error, sendRequest, clearError } = useHttpClient();

  const [formState, inputHandler] = useForm(
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
        // const responseData = await sendRequest(
        //   `${process.env.REACT_APP_BACKEND_URL}/api/login`,
        //   "POST",
        //   JSON.stringify({
        //     uniName: formState.inputs.uniname.value,
        //     userName: formState.inputs.username.value,
        //     userPass: formState.inputs.password.value,
        //   }),
        //   {
        //     "Content-Type": "application/json",
        //   }
        // );

        // TODO : change Dummy DATA
        const responseData = {
          is_first_login: false,
          userId: 1,
          token: "asdf",
          isAdmin: false,
        };

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
        // formData.append("email", formState.inputs.uniName.value);
        // formData.append("name", formState.inputs.userName.value);
        // formData.append("password", formState.inputs.password.value);
        // const responseData = await sendRequest(
        //   `${process.env.REACT_APP_BACKEND_URL}/api/changePW`,
        //   "POST",
        //   formData
        // );
        //비밀번호 변경 후 로그인
        // auth.login(responseData.userId, responseData.token, responseData.isAdmin);
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
        // errorText="Please enter a valid email address."
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
      />
      <Input
        element="input"
        id="password-change"
        type="password"
        placeholder="변경할 비밀번호"
        validators={[VALIDATOR_REQUIRE()]}
        onInput={inputHandler}
      />
      <Input
        element="input"
        id="password-check"
        type="password"
        placeholder="비밀번호 확인"
        validators={[VALIDATOR_REQUIRE()]}
        onInput={inputHandler}
      />
    </React.Fragment>
  );

  return (
    <React.Fragment>
      <ErrorModal error={error} onClear={clearError} />
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
            {isLoading && <LoadingSpinner asOverlay />}
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