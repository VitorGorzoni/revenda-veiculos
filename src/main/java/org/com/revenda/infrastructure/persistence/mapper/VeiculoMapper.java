package org.com.revenda.infrastructure.persistence.mapper;

import org.com.revenda.domain.entity.Veiculo;
import org.com.revenda.infrastructure.persistence.entity.VeiculoJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class VeiculoMapper {

    public VeiculoJpaEntity toJpaEntity(Veiculo veiculo) {
        if (veiculo == null) {
            return null;
        }

        VeiculoJpaEntity entity = new VeiculoJpaEntity();
        entity.setId(veiculo.getId());
        entity.setMarca(veiculo.getMarca());
        entity.setModelo(veiculo.getModelo());
        entity.setAno(veiculo.getAno());
        entity.setCor(veiculo.getCor());
        entity.setPreco(veiculo.getPreco());
        entity.setStatus(veiculo.getStatus());
        entity.setDataCadastro(veiculo.getDataCadastro());

        return entity;
    }

    public Veiculo toDomainEntity(VeiculoJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        Veiculo veiculo = new Veiculo();
        veiculo.setId(entity.getId());
        veiculo.setMarca(entity.getMarca());
        veiculo.setModelo(entity.getModelo());
        veiculo.setAno(entity.getAno());
        veiculo.setCor(entity.getCor());
        veiculo.setPreco(entity.getPreco());
        veiculo.setStatus(entity.getStatus());
        veiculo.setDataCadastro(entity.getDataCadastro());

        return veiculo;
    }
}
