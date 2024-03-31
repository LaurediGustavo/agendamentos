package br.com.tcc.service;

import br.com.tcc.entity.Funcionario;
import br.com.tcc.entity.Ususario;
import br.com.tcc.model.response.FuncionarioResponse;
import br.com.tcc.model.response.UsuarioResponse;
import br.com.tcc.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uteis.Uteis;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioTratarResponse {

    private static final String BASE_IMAGE_PATH = "C:\\img\\";

    public static final String MEDIA_TYPE = "MEDIA_TYPE";

    public static final String RESOURCE = "RESOURCE";

    @Autowired
    private UserRepository userRepository;

    public UsuarioResponse usuarioLogado() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Ususario> usuarioOptional = userRepository.findByUserName(userName);

        if (usuarioOptional.isPresent()) {
            Funcionario funcionario = usuarioOptional.get().getFuncionario();

            if(funcionario != null) {
                return new UsuarioResponse(
                        funcionario.getId(),
                        funcionario.getNome(),
                        funcionario.getSobrenome(),
                        funcionario.getDataDeNascimento(),
                        funcionario.getCpf(),
                        funcionario.getGenero(),
                        funcionario.getTelefone(),
                        funcionario.getCep(),
                        funcionario.getLogradouro(),
                        funcionario.getBairro(),
                        funcionario.getNumero(),
                        funcionario.getBloco(),
                        funcionario.getEmail()
                );
            }
        }

        return null;
    }

    public Map<String, Object> imagem() throws MalformedURLException {
        Ususario ususario = getUsuarioLogado();
        Map<String, Object> mapa = new HashMap<>();

        if (StringUtils.isNotBlank(ususario.getImagePath())) {
            String fileName = Uteis.fileNameFromPath(ususario.getImagePath());
            String imagePath = BASE_IMAGE_PATH + fileName;
            Resource resource = new UrlResource(Paths.get(imagePath).toUri());

            mapa.put(MEDIA_TYPE, Uteis.getMediaType(fileName));
            mapa.put(RESOURCE, resource);
        }

        return mapa;
    }

    private Ususario getUsuarioLogado() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUserName(userName).get();
    }

}
