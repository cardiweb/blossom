package fr.mgargadennec.blossom.core.common.event;


import fr.mgargadennec.blossom.core.common.dto.AbstractAssociationDTO;

public class AfterDissociatedEvent<DTO extends AbstractAssociationDTO> extends Event<DTO> {

  public AfterDissociatedEvent(Object source, DTO entity) {
    super(source, entity);
  }

}
