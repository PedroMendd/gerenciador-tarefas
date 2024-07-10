package com.gerenciador.tarefas.service;

import com.gerenciador.tarefas.entity.Tarefa;
import com.gerenciador.tarefas.repository.GerenciadorTarefasRepositorty;
import com.gerenciador.tarefas.request.AtualizarTarefaRequest;
import com.gerenciador.tarefas.request.CadastrarTarefaRequest;
import com.gerenciador.tarefas.status.TarefaStatusEnum;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class GerenciadorTarefasService {

    @Autowired
    private GerenciadorTarefasRepositorty gerenciadorTarefasRepositorty;

    @Autowired
    private UsuarioService usuarioService;

    public Tarefa salvarTarefa(CadastrarTarefaRequest request) {

        Tarefa tarefa = Tarefa.builder()
                .quantidadeHorasEstimadas(request.getQuantidadeHorasEstimadas())
                .status(TarefaStatusEnum.CRIADA)
                .titulo(request.getTitulo())
                .descricao(request.getDescricao())
                .criador(usuarioService.obterUsuarioId(request.getCriadorId()).get())
                .build();

        return gerenciadorTarefasRepositorty.save(tarefa);
    }

    public Page<Tarefa> obtemTarefasPorTitulo(String titulo, Pageable pageable) {
        return this.gerenciadorTarefasRepositorty.findByTituloContaining(titulo, pageable);
    }

    public Page<Tarefa> obtemTodasTarefas(Pageable pageable) {
        return this.gerenciadorTarefasRepositorty.findAll(pageable);
    }

    public Tarefa atualizarTarefa(Long id, AtualizarTarefaRequest request) {

        Tarefa tarefa = this.gerenciadorTarefasRepositorty.findById(id).get();

        tarefa.setQuantidadeHorasEstimadas(request.getQuantidadeHorasEstimadas());
        tarefa.setStatus(request.getStatus());
        tarefa.setTitulo(request.getTitulo());
        tarefa.setDescricao(request.getDescricao());
        tarefa.setResponsavel(usuarioService.obterUsuarioId(request.getResponsavelId()).get());
        tarefa.setQuantidadeHorasRealizada(request.getQuantidadeHorasRealizada());


        this.gerenciadorTarefasRepositorty.save(tarefa);

        return tarefa;
    }

    public void excluirTarefa(Long id) {
        this.gerenciadorTarefasRepositorty.deleteById(id);
    }


}
