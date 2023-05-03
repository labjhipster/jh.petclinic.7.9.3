import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { VisitFormService, VisitFormGroup } from './visit-form.service';
import { IVisit } from '../visit.model';
import { VisitService } from '../service/visit.service';
import { IPet } from 'app/entities/pet/pet.model';
import { PetService } from 'app/entities/pet/service/pet.service';

@Component({
  selector: 'jhi-visit-update',
  templateUrl: './visit-update.component.html',
})
export class VisitUpdateComponent implements OnInit {
  isSaving = false;
  visit: IVisit | null = null;

  petsSharedCollection: IPet[] = [];

  editForm: VisitFormGroup = this.visitFormService.createVisitFormGroup();

  constructor(
    protected visitService: VisitService,
    protected visitFormService: VisitFormService,
    protected petService: PetService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePet = (o1: IPet | null, o2: IPet | null): boolean => this.petService.comparePet(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visit }) => {
      this.visit = visit;
      if (visit) {
        this.updateForm(visit);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const visit = this.visitFormService.getVisit(this.editForm);
    if (visit.id !== null) {
      this.subscribeToSaveResponse(this.visitService.update(visit));
    } else {
      this.subscribeToSaveResponse(this.visitService.create(visit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVisit>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(visit: IVisit): void {
    this.visit = visit;
    this.visitFormService.resetForm(this.editForm, visit);

    this.petsSharedCollection = this.petService.addPetToCollectionIfMissing<IPet>(this.petsSharedCollection, visit.pet);
  }

  protected loadRelationshipsOptions(): void {
    this.petService
      .query()
      .pipe(map((res: HttpResponse<IPet[]>) => res.body ?? []))
      .pipe(map((pets: IPet[]) => this.petService.addPetToCollectionIfMissing<IPet>(pets, this.visit?.pet)))
      .subscribe((pets: IPet[]) => (this.petsSharedCollection = pets));
  }
}
