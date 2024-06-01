import React, { useState, forwardRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, Button, Container, IconButton, InputAdornment, Snackbar, TextField, Typography } from '@mui/material';
import MuiAlert from '@mui/material/Alert';
import loginImage from '../../../assets/logo2.png';
import Slide from '@mui/material/Slide';
import './resetPassword.scss';
import { Link } from 'react-router-dom';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import api from '../../../services/api';
import { useLocation } from 'react-router-dom';

const Alert = forwardRef(function Alert(props, ref) {
    return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

const TransitionLeft = (props) => {
    return <Slide {...props} direction="left" />;
};

export const ResetPassword = () => {
    const navigate = useNavigate();
    const [open, setOpen] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [success, setSuccess] = useState(false);

    const location = useLocation();
    const email = location.state?.email || '';

    const vertical = "top";
    const horizontal = "right";

    const handleClickShowPassword = () => {
        setShowPassword(!showPassword);
    };

    const handleClickShowConfirmPassword = () => {
        setShowConfirmPassword(!showConfirmPassword);
    };

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        const password = event.target.password.value;
        const confirmPassword = event.target.confirmPassword.value;

        if (password.length < 5) {
            setErrorMessage("A senha deve conter no mínimo 5 caracteres.");
            setOpen(true);
            return;
        } else if (password !== confirmPassword) {
            setErrorMessage("As senhas não coincidem.");
            setOpen(true);
            return;
        }

        restPassword(password, confirmPassword);
    };

    const restPassword = async (novaSenha, novaSenhaConfirmacao) => {
        try {            
            await api.post("/rest-password/reset", {
                email: email,
                novaSenha: novaSenha,
                novaSenhaConfirmacao: novaSenhaConfirmacao
            });

            setSuccess(true);
        } catch (error) {
            if (error.response && error.response.status === 400) {
                setErrorMessage(error.response.data.errors[0].message);
                setOpen(true);
                return;
            }
            else {
                throw new Error("Erro ao resetar a senha: " + error.message);
            }
        }
    };

    const handleClose = (event, reason) => {
        if (reason === "clickaway") {
            return;
        }
        setOpen(false);
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
                    <Typography variant="body1">
                        {errorMessage}
                    </Typography>
                </Alert>
            </Snackbar>

            <div className="reset-password-container">
                <img src={loginImage} alt="Logo da Empresa" className="logo" />
                <Box className="box">
                    <Container maxWidth="xs">
                        {success ? (
                            <>
                                <Typography component="h1" variant="h4" gutterBottom sx={{ mb: 3 }}>
                                    Senha redefinida com sucesso!
                                </Typography>
                                <Typography variant="body1" sx={{ mb: 3 }}>
                                    Sua senha foi redefinida com sucesso. Agora você pode fazer login com sua nova senha.
                                </Typography>
                            </>
                        ) : (
                            <>
                                <Typography component="h1" variant="h4" gutterBottom sx={{ mb: 3 }}>
                                    Redefinir Senha
                                </Typography>
                                <Typography variant="body1" sx={{ mb: 3 }}>
                                    Por favor, insira sua nova senha abaixo.
                                </Typography>
                                <form onSubmit={handleSubmit}>
                                    <TextField
                                        fullWidth
                                        id="password"
                                        label="Nova Senha"
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
                                            mt: 2,
                                            mb: 1,
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
                                    <TextField
                                        fullWidth
                                        id="confirmPassword"
                                        label="Confirmar Nova Senha"
                                        name="confirmPassword"
                                        type={showConfirmPassword ? 'text' : 'password'}
                                        required
                                        InputProps={{
                                            endAdornment: (
                                                <InputAdornment position="end">
                                                    <IconButton
                                                        aria-label="toggle password visibility"
                                                        onClick={handleClickShowConfirmPassword}
                                                        onMouseDown={handleMouseDownPassword}
                                                    >
                                                        {showConfirmPassword ? <VisibilityOff style={{ color: '#2c9cac' }} /> : <Visibility style={{ color: '#2c9cac' }} />}
                                                    </IconButton>
                                                </InputAdornment>
                                            ),
                                        }}
                                        sx={{
                                            mt: 2,
                                            mb: 3,
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
                                    <Button
                                        type="submit"
                                        fullWidth
                                        variant="contained"
                                        size="large"
                                        sx={{
                                            mt: 2,
                                            mb: 3,
                                            borderRadius: 28,
                                            color: "#fff",
                                            backgroundColor: "#2c9cac",
                                            "&:hover": {
                                                backgroundColor: "#1b819b",
                                            },
                                        }}
                                        className="reset-password-button"
                                    >
                                        Redefinir Senha
                                    </Button>
                                </form>
                            </>
                        )}
                        {success ? (
                            <Button
                                component={Link}
                                to="/login"
                                fullWidth
                                variant="contained"
                                size="large"
                                sx={{
                                    mt: 2,
                                    mb: 3,
                                    borderRadius: 28,
                                    color: "#fff",
                                    backgroundColor: "#2c9cac",
                                    "&:hover": {
                                        backgroundColor: "#1b819b",
                                    },
                                }}
                            >
                                Fazer Login
                            </Button>
                        ) : (
                            <Typography variant="body2" component={Link} to="/login" style={{ textDecoration: 'none', display: 'block', textAlign: 'center', marginTop: '10px', color: '#2c9cac' }}>
                                Voltar para a página de login
                            </Typography>
                        )}
                    </Container>
                </Box>
            </div>
        </>
    );
};
