import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISpecialty } from '../specialty.model';
import { SpecialtyService } from '../service/specialty.service';

@Injectable({ providedIn: 'root' })
export class SpecialtyRoutingResolveService implements Resolve<ISpecialty | null> {
  constructor(protected service: SpecialtyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISpecialty | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((specialty: HttpResponse<ISpecialty>) => {
          if (specialty.body) {
            return of(specialty.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
