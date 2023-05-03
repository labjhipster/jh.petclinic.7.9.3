import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpecialty } from '../specialty.model';
import { SpecialtyService } from '../service/specialty.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './specialty-delete-dialog.component.html',
})
export class SpecialtyDeleteDialogComponent {
  specialty?: ISpecialty;

  constructor(protected specialtyService: SpecialtyService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.specialtyService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
