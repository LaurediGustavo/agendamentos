import { React, forwardRef, useState, useEffect } from "react";
import Typography from "@mui/material/Typography";
import { ThemeProvider, createTheme } from "@mui/material/styles";
import { Avatar, Box, Button, Container, Grid, Snackbar, TextField } from "@mui/material";
import Stack from "@mui/material/Stack";
import { useNavigate } from "react-router-dom";
import loginImage from '../../../assets/login.png';
import MuiAlert from '@mui/material/Alert';
import Slide from '@mui/material/Slide';
import api from '../../../services/api';
import { login, isAuthenticated } from '../../../services/auth_service'; // Adicionado isAuthenticated
import './login.scss';

const Alert = forwardRef(function Alert(props, ref) {
    return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

const darkTheme = createTheme({
    palette: {
        mode: "dark",
    },
});

export const Login = () => {
    const navigate = useNavigate();
    const [open, setOpen] = useState(false);
    const vertical = "top";
    const horizontal = "right";
    const [errorMessage, setErrorMessage] = useState("");

    function TransitionLeft(props) {
        return <Slide {...props} direction="left" />
    }

    useEffect(() => { // Adicionado para verificar se o usuário está autenticado e redirecionar
        if (isAuthenticated()) {
            navigate("/"); // Redirecionar para a página inicial se o usuário já estiver autenticado
        }
    }, []);

    const handleSubmit = async (event) => {
        event.preventDefault();

        const username = event.target.username.value;
        const password = event.target.password.value;

        // Validar o formato do e-mail
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!username || !password) {
            setErrorMessage("Falha no login! Entre com o usuário e senha corretos.");
            setOpen(true);
            return;
        }

        try {
            const result = await api.post("auth/login", {
                userName: username,
                password: password
            });
            login(result.data.usuarioId ,result.data.tokenJwt, result.data.roles);
            // Verificar os papéis do usuário e redirecionar para a página apropriada
            const roles = result.data.roles; // Obtém os papéis do usuário do resultado do login
            if (roles.includes("ROLE_DOUTOR")) {
                navigate("/");
            } else {
                navigate("/");
            }
        } catch (error) {
            if (error.response && error.response.status === 401) {
                setErrorMessage("Falha no login! Entre com o usuário e senha corretos.");
            } else {
                setErrorMessage("Erro no serviço de login!");
            }
            setOpen(true);
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
                    backgroundColor: "#c5edf5",
                    height: "100vh",
                    color: "#f5f5f5"
                }}
            >
                <Box className="boxStyle">
                    <Grid container>
                        <Grid item xs={12} sm={12} lg={7}>
                            <Box
                                className="backgroundImage"
                                style={{
                                    backgroundImage: `url(${loginImage})`,
                                }}
                            >
                            </Box>
                        </Grid>
                        <Grid item xs={12} sm={12} lg={5}>
                            <Box className="rightBox">
                                <ThemeProvider theme={darkTheme}>
                                    <Container>
                                        <Box />
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
                                                        label="E-mail"
                                                        name="username"
                                                        autoComplete="email"
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