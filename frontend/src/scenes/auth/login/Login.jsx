import { React, forwardRef, useState } from "react";
import Typography from "@mui/material/Typography";
import { ThemeProvider, createTheme } from "@mui/material/styles";
import { Avatar, Box, Button, Container, Grid, Snackbar, TextField } from "@mui/material";
import Stack from "@mui/material/Stack";
import { useNavigate } from "react-router-dom";
import loginImage from '../../../assets/login.png';
import MuiAlert from '@mui/material/Alert';
import Slide from '@mui/material/Slide';
import api from '../../../services/api';
import { login } from '../../../services/auth_service';

const Alert = forwardRef(function Alert(props, ref) {
    return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
})

const darkTheme = createTheme({
    palette: {
        mode: "dark",
    },
});

const boxsyle = {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: "75%",
    height: "70%",
    bgcolor: "background.paper",
    boxShadow: 24,
}

export const Login = () => {
    const navigate = useNavigate();
    const [open, setOpen] = useState(false);
    const vertical = "top";
    const horizontal = "right";
    const [errorMessage, setErrorMessage] = useState("");

    function TransitionLeft(props) {
        return <Slide {...props} direction="left" />
    }

    const handleSubmit = async (event) => {
      event.preventDefault();
  
      const username = event.target.username.value;
      const password = event.target.password.value;
  
      if (!username || !password) {
          setErrorMessage("Falha no login! Entre com o usuário e senha corretos.");
          setOpen(true);
      } else {
          try {
              const result = await api.post("auth/login", {
                  userName: username,
                  password: password
              });
              login(result.data.tokenJwt, result.data.roles); // Certifique-se de acessar tokenJwt e roles de result.data
              navigate("/");
          } catch (error) {
              if (error.response && error.response.status === 401) {
                  setErrorMessage("Falha no login! Entre com o usuário e senha corretos.");
              } else {
                  setErrorMessage("Erro no serviço de login!");
              }
              setOpen(true);
          }
      }
  };

    const handleClose = (event, reason) => {
        if (reason === "clickaway") {
            return;
        }
        setOpen(false);
    }

    return (
        <>
            <Snackbar
                open={open}
                autoHideDuration={3000}
                onClose={handleClose}
                TransitionComponent={TransitionLeft}
                anchorOrigin={{ vertical, horizontal }}
            >
                <Alert onClose={handleClose} severity="error" sx={{ width: "100%" }}>
                    {errorMessage}
                </Alert>
            </Snackbar>

            <div
                style={{
                    backgroundColor: "white",
                    backgroundSize: "cover",
                    height: "100vh",
                    color: "#f5f5f5"
                }}
            >
                <Box sx={boxsyle}>
                    <Grid container>
                        <Grid item xs={12} sm={12} lg={6}>
                            <Box
                                style={{
                                    backgroundImage: `url(${loginImage})`,
                                    backgroundSize: "cover",
                                    backgroundPosition: 'center',
                                    height: "63vh",
                                    color: "#f5f5f5",
                                    marginTop: '30px',
                                }}
                            >
                            </Box>
                        </Grid>
                        <Grid item xs={12} sm={12} lg={6}>
                            <Box
                                style={{
                                    backgroundSize: "cover",
                                    height: "103%",
                                    minHeight: "100%",
                                    backgroundColor: "#3b33d5",
                                }}
                            >
                                <ThemeProvider theme={darkTheme}>
                                    <Container>
                                        <Box height={65} />
                                        <Box
                                            display="flex"
                                            flexDirection="column"
                                            alignItems="center"
                                            marginBottom="20px"
                                        >
                                            <Avatar sx={{ mb: "4px", bgcolor: "#ffffff" }} />
                                            <Typography component="h1" variant="h4">
                                                Login
                                            </Typography>
                                        </Box>
                                        <Box
                                            component="form"
                                            noValidate
                                            onSubmit={handleSubmit}
                                            sx={{ mt: 2 }}
                                        >
                                            <Grid container spacing={1}>
                                                <Grid item xs={12} sx={{ ml: "3em", mr: "3em", mb: "5px" }}>
                                                    <TextField
                                                        fullWidth
                                                        id="username"
                                                        label="Username"
                                                        name="username"
                                                        autoComplete="username"
                                                    />
                                                </Grid>
                                                <Grid item xs={12} sx={{ ml: "3em", mr: "3em" }}>
                                                    <TextField
                                                        fullWidth
                                                        id="password"
                                                        label="Senha"
                                                        name="password"
                                                        autoComplete="password"
                                                        type="password"
                                                    />
                                                </Grid>
                                                <Grid item xs={12} sx={{ ml: "3em", mr: "3em" }}>
                                                    <Stack direction="row" spacing={1}>
                                                        <Typography
                                                            variant="body2"
                                                            component="span"
                                                            onClick={() => {
                                                                navigate("/reset-password")
                                                            }}
                                                            style={{ marginTop: "10px", cursor: "pointer" }}
                                                        >
                                                            Esqueci minha senha
                                                        </Typography>
                                                    </Stack>
                                                </Grid>
                                                <Grid item xs={12} sx={{ mt: "20px", ml: "3em", mr: "3em" }}>
                                                    <Button
                                                        type="submit"
                                                        variant="contained"
                                                        fullWidth
                                                        size="large"
                                                        sx={{
                                                            mt: "10px",
                                                            mr: "20px",
                                                            borderRadius: 28,
                                                            color: "#ffffff",
                                                            minWidth: "170px",
                                                            backgroundColor: "#7B68EE",
                                                        }}
                                                    >
                                                        Login
                                                    </Button>
                                                </Grid>
                                            </Grid>
                                        </Box>
                                    </Container>
                                </ThemeProvider>
                            </Box>
                        </Grid>
                    </Grid>
                </Box>
            </div>
        </>
    );
}
