import { createContext } from "react";

export const AuthContext = createContext({
  isLoggedIn: false,
  userId: null,
  token: null,
  login: () => {},
  logout: () => {},
  isAdmin: false,
  isFirstLogin: true,
  tokenExpirationDate: null,
});
