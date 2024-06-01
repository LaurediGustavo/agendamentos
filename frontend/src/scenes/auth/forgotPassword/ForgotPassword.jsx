import { React, forwardRef, useState } from "react";
import Typography from "@mui/material/Typography";
import { Box, Button, Container, Snackbar, TextField } from "@mui/material";
import { useNavigate } from "react-router-dom";
import MuiAlert from '@mui/material/Alert';
import loginImage from '../../../assets/logo2.png';
import Slide from '@mui/material/Slide';
import './forgotPassword.scss';
import { Link } from 'react-router-dom';
import api from '../../../services/api';

const Alert = forwardRef(function Alert(props, ref) {
    return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

const TokenField = () => {
    const [token, setToken] = useState(Array(5).fill(''));

    const handlePaste = (e) => {
        e.preventDefault();
        const pastedData = e.clipboardData.getData('text');
        if (pastedData && pastedData.length === 5) {
            const newToken = pastedData.split('');
            setToken(newToken);
        }
    };

    const handleChange = (index) => (e) => {
        let newToken = [...token];
        if (!isNaN(e.target.value) && e.target.value.length <= 1) {
            newToken[index] = e.target.value;
            setToken(newToken);
            if (e.target.value && index < 4) {
                document.getElementById(`token-field-${index + 1}`).focus();
            }
        }
    };

    const handleKeyDown = (index) => (e) => {
        if (e.key === 'Backspace' && !token[index] && index > 0) {
            document.getElementById(`token-field-${index - 1}`).focus();
        }
    };

    return (
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
            <div style={{ display: 'flex', justifyContent: 'center', maxWidth: 300 }} onPaste={handlePaste}>
                {token.map((num, index) => (
                    <TextField
                        key={index}
                        id={`token-field-${index}`}
                        type="text"
                        value={num}
                        onChange={handleChange(index)}
                        onKeyDown={handleKeyDown(index)}
                        style={{ margin: '0 10px', marginBottom: '20px' }}
                    />
                ))}
            </div>
        </div>
    );
};


export const ForgotPassword = () => {
    const navigate = useNavigate();
    const [open, setOpen] = useState(false);
    const [success, setSuccess] = useState(false); 
    const vertical = "top";
    const horizontal = "right";
    const [errorMessage, setErrorMessage] = useState("");
    const [email, setEmail] = useState("");

    function TransitionLeft(props) {
        return <Slide {...props} direction="left" />
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        
        if (!success) {
            const email = event.target.email.value;
        
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email)) {
                setErrorMessage("Formato de e-mail inválido. Por favor, insira um e-mail válido.");
                setOpen(true);
                return;
            }

            sendEmail(email);
        }  else {
            const token = document.querySelectorAll("[id^='token-field']").values();
            const tokenValue = [...token].map(input => input.value).join('');
            
            if (tokenValue.length !== 5) {
                setErrorMessage("Por favor, preencha o token completo.");
                setOpen(true);
                return;
            }

            validatorToken(email, tokenValue)
        }
    };

    const sendEmail = async (email) => {
        try {
            await api.post("/rest-password/send-email", {
                email: email
            });

            setEmail(email);
            setSuccess(true);
        } catch (error) {
            if (error.response && error.response.status === 400) {
                setErrorMessage(error.response.data.errors[0].message);
                setOpen(true);
                return;
            }
            else {
                throw new Error("Erro ao enviar o e-mail: " + error.message);
            }
        }
    };

    const validatorToken = async (email, token) => {
        try {
            await api.post("/rest-password/validator-code", {
                email: email,
                codigo: token
            });

            // Se o token for válido, redirecione o usuário para a página de redefinição de senha
            navigate('/reset-password', { state: { email } }); // Redireciona 
        } catch (error) {
            if (error.response && error.response.status === 400) {
                setErrorMessage(error.response.data.errors[0].message);
                setOpen(true);
                return;
            }
            else {
                throw new Error("Erro ao validar o token: " + error.message);
            }
        }
    };

    const handleClose = (event, reason) => {
        if (reason === "clickaway") {
            return;
        }
        setOpen(false);
    }

    const handleChangeEmail = () => {
        setSuccess(false);
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
                <Alert onClose={handleClose} severity={errorMessage ? "error" : "success"} sx={{ width: "100%" }}>
                    <Typography variant="body1" >
                        {errorMessage}
                    </Typography>
                </Alert>
            </Snackbar>

            <div className="forgot-password-container">
                <img src={loginImage} alt="Logo da Empresa" className="logo" />
                <Box className="box">
                    <Container maxWidth="xs">
                        <Typography component="h1" variant="h4" gutterBottom sx={{ mb: 3 }}>
                            {success ? "Preencher o token" : "Esqueci minha senha"}
                        </Typography>
                        <Typography variant="body1" sx={{ mb: 3 }}>
                            {success ? "Um token foi enviado para o seu e-mail. Por favor, insira o token abaixo para redefinir sua senha." : "Por favor, insira seu e-mail abaixo para enviarmos as instruções de redefinição de senha."}
                        </Typography>
                        <form onSubmit={handleSubmit}>
                            {success ? (
                                <TokenField />
                            ) : (
                                <TextField
                                    fullWidth
                                    id="email"
                                    label="E-mail"
                                    name="email"
                                    autoComplete="email"
                                    required
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
                            )}
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
                                className="forgot-password-button"
                            >
                                Enviar
                            </Button>
                        </form>

                        {success && (
                            <Typography
                                variant="body2"
                                onClick={handleChangeEmail}
                                style={{ textDecoration: 'none', display: 'block', textAlign: 'center', marginBottom: '15px', color: '#2c9cac', cursor: 'pointer' }}
                            >
                                Alterar e-mail
                            </Typography>
                        )}


                        <Typography variant="body2" component={Link} to="/login" style={{ textDecoration: 'none', display: 'block', textAlign: 'center', marginTop: '10px', color: '#2c9cac' }}>
                            Voltar para a página de login
                        </Typography>


                    </Container>
                </Box>
            </div>
        </>
    );
}
