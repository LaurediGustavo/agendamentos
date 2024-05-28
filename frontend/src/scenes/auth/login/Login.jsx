import { React, forwardRef, useState, useEffect } from "react";
import Typography from "@mui/material/Typography";
import { ThemeProvider, createTheme } from "@mui/material/styles";
import { Avatar, Box, Button, Container, Grid, Snackbar, TextField, InputAdornment, IconButton } from "@mui/material";
import Stack from "@mui/material/Stack";
import { useNavigate } from "react-router-dom";
import loginImage from '../../../assets/login.png';
import MuiAlert from '@mui/material/Alert';
import Slide from '@mui/material/Slide';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import api from '../../../services/api';
import { login, isAuthenticated } from '../../../services/auth_service';
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
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    function TransitionLeft(props) {
        return <Slide {...props} direction="left" />
    }

    useEffect(() => {
        if (isAuthenticated()) {
            navigate("/");
        }
    }, []);

    const handleSubmit = async (event) => {
        event.preventDefault();

        const username = event.target.username.value;
        const password = event.target.password.value;

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
            login(result.data.userName, result.data.usuarioId, result.data.tokenJwt, result.data.roles);

            const roles = result.data.roles;
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

    const handleClickShowPassword = () => {
        setShowPassword(!showPassword);
    };

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

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
                    backgroundColor: "#F5FDFC",
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
                                    backgroundSize: 'cover',
                                    backgroundPosition: 'center',
                                    backgroundRepeat: 'no-repeat',
                                    height: '100%',
                                    width: '100%',
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
                                            <Avatar sx={{ mb: "4px", bgcolor: "#e8f9f7" }} />
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
                                                <Grid item xs={12} sx={{ ml: "3em", mr: "3em", mb: "5px", }}>
                                                    <TextField
                                                        fullWidth
                                                        id="username"
                                                        label="E-mail"
                                                        name="username"
                                                        required
                                                        autoComplete="email"
                                                        sx={{
                                                            '& label': {
                                                                color: '#2c9cac',
                                                            },
                                                            '& label.Mui-focused': {
                                                                color: '#2c9cac',
                                                            },
                                                            '& .MuiInput-underline:after': {
                                                                borderBottomColor: '#2c9cac',
                                                            },
                                                            '& .MuiOutlinedInput-root': {
                                                                '& fieldset': {
                                                                    borderColor: '#2c9cac',
                                                                },
                                                                '&:hover fieldset': {
                                                                    borderColor: '#2c9cac',
                                                                },
                                                                '&.Mui-focused fieldset': {
                                                                    borderColor: '#308C8F',
                                                                },
                                                            },
                                                            '& .MuiInputBase-input': {
                                                                color: '#308C8F',
                                                            }
                                                        }}
                                                    />
                                                </Grid>
                                                <Grid item xs={12} sx={{ ml: "3em", mr: "3em" }}>
                                                    <TextField
                                                        fullWidth
                                                        id="password"
                                                        label="Senha"
                                                        name="password"
                                                        type={showPassword ? 'text' : 'password'}
                                                        required
                                                        InputProps={{
                                                            endAdornment: (
                                                                <InputAdornment position="end">
                                                                    <IconButton
                                                                        aria-label="toggle password visibility"
                                                                        onClick={handleClickShowPassword}
                                                                        onMouseDown={handleMouseDownPassword}
                                                                    >
                                                                        {showPassword ? <VisibilityOff style={{ color: '#2c9cac' }} /> : <Visibility style={{ color: '#2c9cac' }} />}
                                                                    </IconButton>
                                                                </InputAdornment>
                                                            ),
                                                        }}
                                                        sx={{
                                                            '& label': {
                                                                color: '#2c9cac',
                                                            },
                                                            '& label.Mui-focused': {
                                                                color: '#2c9cac',
                                                            },
                                                            '& .MuiInput-underline:after': {
                                                                borderBottomColor: '#2c9cac',
                                                            },
                                                            '& .MuiOutlinedInput-root': {
                                                                '& fieldset': {
                                                                    borderColor: '#2c9cac',
                                                                },
                                                                '&:hover fieldset': {
                                                                    borderColor: '#2c9cac',
                                                                },
                                                                '&.Mui-focused fieldset': {
                                                                    borderColor: '#308C8F',
                                                                },
                                                            },
                                                            '& .MuiInputBase-input': {
                                                                color: '#308C8F',
                                                            }
                                                        }}
                                                    />
                                                </Grid>
                                                <Grid item xs={12} sx={{ ml: "3em", mr: "3em" }}>
                                                    <Stack direction="row" spacing={1}>
                                                        <Typography
                                                            variant="body2"
                                                            component="span"
                                                            onClick={() => {
                                                                navigate("/forgot-password");
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
                                                            color: "#fff",
                                                            minWidth: "170px",
                                                            backgroundColor: "#2c9cac",
                                                            "&:hover": {
                                                                backgroundColor: "#1b819b",
                                                            },

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