import { useState, useCallback, useEffect } from "react";

let logoutTimer;

export const useAuth = () => {
  const [token, setToken] = useState(false);
  const [tokenExpirationDate, setTokenExpirationDate] = useState();
  const [userId, setUserId] = useState(false);
  const [isAdmin, setIsAdmin] = useState(false);
  const [isFirstLogin, setIsFirstLogin] = useState(false);

  const login = useCallback(
    (uid, token, isAdmin, isFirstLogin, expirationDate) => {
      setUserId(uid);
      setIsAdmin(isAdmin);
      setIsFirstLogin(isFirstLogin);
      const tokenExpirationDate =
        expirationDate || new Date(new Date().getTime() + 1000 * 60 * 60 * 2);
      setTokenExpirationDate(tokenExpirationDate);
      setToken(token);
      localStorage.setItem(
        "userData",
        JSON.stringify({
          userId: uid,
          token: token,
          isAdmin: isAdmin,
          isFirstLogin: isFirstLogin,
          expiration: tokenExpirationDate.toISOString(),
        })
      );
    },
    []
  );

  const logout = useCallback(() => {
    setToken(null);
    setTokenExpirationDate(null);
    setUserId(null);
    localStorage.removeItem("userData");
  }, []);

  useEffect(() => {
    if (token && tokenExpirationDate) {
      const remainingTime =
        tokenExpirationDate.getTime() - new Date().getTime();
      logoutTimer = setTimeout(() => {
        logout();
        alert("토큰 시간이 만료되어 로그인되었습니다. 재로그인해주세요.");
      }, remainingTime);
    } else {
      clearTimeout(logoutTimer);
    }
  }, [token, logout, tokenExpirationDate]);

  useEffect(() => {
    const storedData = JSON.parse(localStorage.getItem("userData"));
    if (
      storedData &&
      storedData.token &&
      new Date(storedData.expiration) > new Date()
    ) {
      login(
        storedData.userId,
        storedData.token,
        storedData.isAdmin,
        storedData.isFirstLogin,
        new Date(storedData.expiration)
      );
    }
  }, [login]);

  return {
    token,
    login,
    logout,
    userId,
    isAdmin,
    isFirstLogin,
    tokenExpirationDate,
  };
};
