import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SpecialtyFormService } from './specialty-form.service';
import { SpecialtyService } from '../service/specialty.service';
import { ISpecialty } from '../specialty.model';

import { SpecialtyUpdateComponent } from './specialty-update.component';

describe('Specialty Management Update Component', () => {
  let comp: SpecialtyUpdateComponent;
  let fixture: ComponentFixture<SpecialtyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let specialtyFormService: SpecialtyFormService;
  let specialtyService: SpecialtyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SpecialtyUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SpecialtyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SpecialtyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    specialtyFormService = TestBed.inject(SpecialtyFormService);
    specialtyService = TestBed.inject(SpecialtyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const specialty: ISpecialty = { id: 456 };

      activatedRoute.data = of({ specialty });
      comp.ngOnInit();

      expect(comp.specialty).toEqual(specialty);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpecialty>>();
      const specialty = { id: 123 };
      jest.spyOn(specialtyFormService, 'getSpecialty').mockReturnValue(specialty);
      jest.spyOn(specialtyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ specialty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: specialty }));
      saveSubject.complete();

      // THEN
      expect(specialtyFormService.getSpecialty).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(specialtyService.update).toHaveBeenCalledWith(expect.objectContaining(specialty));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpecialty>>();
      const specialty = { id: 123 };
      jest.spyOn(specialtyFormService, 'getSpecialty').mockReturnValue({ id: null });
      jest.spyOn(specialtyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ specialty: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: specialty }));
      saveSubject.complete();

      // THEN
      expect(specialtyFormService.getSpecialty).toHaveBeenCalled();
      expect(specialtyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpecialty>>();
      const specialty = { id: 123 };
      jest.spyOn(specialtyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ specialty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(specialtyService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
